#ifndef __PROGTEST__
#include <cstring>
#include <iostream>
#include <algorithm>
#include <vector>
#include <array>
#include <iterator>
#include <set>
#include <list>
#include <map>
#include <unordered_set>
#include <unordered_map>
#include <deque>
#include <memory>
#include <thread>
#include <mutex>
#include <atomic>
#include "sample_tester.h"
using namespace std;
#endif /* __PROGTEST__ */

struct CElem {
    int m_Fee = 0;
    uint64_t m_First = 0, m_Second = 0;

    void set ( size_t pos ) {
        if ( pos >= 64 )
            m_Second |= 1UL << ( pos - 64 );
        else
            m_First |= 1UL << pos;
    }
    bool operator [] ( size_t pos ) const {
        if ( pos >= 64 )
            return (m_Second >> (pos - 64)) & 1U;
        return (m_First >> pos) & 1U;
    }
};

class CKnapSack {
    mutex              * m_Mtxs;
    condition_variable * m_Conds;
    atomic_int         * m_Ready;
    CElem **             T;
public:
    AShip              m_Ship;
    vector<CCargo>     m_Cargo;

    CKnapSack ( const AShip & ship, const vector<CCargo> & cargo )
            : m_Ship(ship), m_Cargo(cargo) {
        m_Mtxs  = new mutex [ m_Cargo.size() + 1 ];
        m_Conds = new condition_variable [ m_Cargo.size() + 1 ];
        m_Ready = new atomic_int [ m_Cargo.size() + 1 ];

        T       = new CElem * [ m_Cargo.size() + 1 ];
        for ( size_t i = 0; i <= m_Cargo.size(); ++i ) {
            m_Ready[i] = -1;
            T[i] = new CElem [ (ship->MaxWeight() + 1) * (ship->MaxVolume() + 1) ];
        }
    }

    ~CKnapSack () {
        delete [] T [ m_Cargo.size() ];
        delete [] T;
    }

    bool Compute ( size_t line, int & tmpWeight, vector<CCargo> & tmp ) {

        for ( int weight = 0; weight <= m_Ship->MaxWeight(); ++weight ) {
            for ( int volume = 0; volume <= m_Ship->MaxVolume(); ++volume ) {
                int pos = weight * (m_Ship->MaxVolume() + 1) + volume;

                // Check position
                if ( line > 0 && m_Ready[line - 1] < pos ) {
                    unique_lock ul ( m_Mtxs[line] );
                    m_Conds[line - 1].wait(ul, [this, line, pos] () { return m_Ready[line - 1] >= pos; } );
                }

                // We can fill
                if (line == 0 || weight == 0 || volume == 0)
                    T[line][pos] = {0, 0, 0};
                else if (m_Cargo[line - 1].m_Weight <= weight && m_Cargo[line - 1].m_Volume <= volume) {
                    int pos2 = (weight - m_Cargo[line - 1].m_Weight) * ( m_Ship->MaxVolume() + 1 ) + (volume - m_Cargo[line - 1].m_Volume);
                    int top = max(m_Cargo[line - 1].m_Fee + T[line - 1][pos2].m_Fee,T[line - 1][pos].m_Fee);
                    if ( top == T[line - 1][pos].m_Fee )
                        T[line][pos] = T[line - 1][pos];
                    else {
                        T[line][pos] = T[line - 1][pos2];
                        T[line][pos].m_Fee += m_Cargo[line - 1].m_Fee;
                        T[line][pos].set(line - 1);
                    }
                }
                else
                    T[line][pos] = T[line - 1][pos];

                // Notify others
                if ( pos % 100 == 0 ) {
                    unique_lock ul ( m_Mtxs[line] );
                    m_Ready[line] = pos;
                    m_Conds[line].notify_all();
                }
            }
        }

        // Notify remaining
        unique_lock ul ( m_Mtxs[line] );
        m_Ready[line] = m_Ship->MaxWeight() * (m_Ship->MaxWeight() + 1) * (m_Ship->MaxVolume() + 1);
        m_Conds[line].notify_all();

        // Free memory
        if (line > 0)
            delete [] T[line - 1];

        // We're not done
        if ( line != m_Cargo.size() )
            return false;

        // We are done, print result
        CElem res = T[m_Cargo.size()][m_Ship->MaxWeight() * ( m_Ship->MaxVolume() + 1 ) + m_Ship->MaxVolume()];
        tmpWeight = res.m_Fee;

        for ( size_t i = 0; i < m_Cargo.size(); ++i )
            if ( res[i] )
                tmp.push_back(m_Cargo[i]);
        return true;
    }
};

using AKnapSack = shared_ptr<CKnapSack>;

class DummyShip : public CShip {
public:
    DummyShip ( int maxWeight, int maxVolume ) : CShip("", maxWeight, maxVolume) {}
    void Load(const std::vector<CCargo> &cargo) override {}
};

class CCargoPlanner {

    // common
    vector<thread> m_Customers;
    vector<thread>   m_Workers;
    vector<ACustomer> m_People;

    // customer -> ship
    mutex m_ProcessMtx;
    condition_variable m_ProcessEmpty;
    deque<pair<AShip, ACustomer>> m_Process;

    // helper customer -> ship
    mutex m_LoadMtx;
    map<AShip, pair<size_t, vector<CCargo>>> m_Load;

    // cargo -> ship
    mutex m_RealLoadMtx;
    condition_variable m_RealLoadEmpty;
    deque<pair<AKnapSack, int>> m_RealLoad;

    // === CUSTOMER -> SHIP
    pair<AShip, ACustomer> removeShipCustomer () {
        pair<AShip, ACustomer> process;
        unique_lock prUl ( m_ProcessMtx );
        m_ProcessEmpty.wait(prUl, [ this ] () { return !m_Process.empty(); } );
        process = m_Process.front();
        if ( process.first )
            m_Process.pop_front();
        return process;
    }

    bool CustomerRequest ( const pair<AShip, ACustomer> & process, vector<CCargo> & result ) {
        // Get wanted
        vector<CCargo> wanted;
        process.second->Quote(process.first->Destination(), wanted);

        // Create ship record
        unique_lock ldUl ( m_LoadMtx );

        auto it = m_Load.find(process.first);
        if ( it == m_Load.end() )
            it = m_Load.insert({process.first, make_pair(0, vector<CCargo>())}).first;

        it->second.first += 1;
        move(wanted.begin(), wanted.end(), back_inserter(it->second.second));

        if ( it->second.first != m_People.size() )
            return false;

        // Everyone gave their order on the ship
        swap(result, it->second.second);
        m_Load.erase(it);
        return true;
    }

    void CustomerThread () {
        unique_lock prUl ( m_ProcessMtx );
        while ( true ) {
            prUl.unlock();
            pair<AShip, ACustomer> process = removeShipCustomer();

            if ( !process.first ) // end signal
                break;

            vector<CCargo> result;
            if ( CustomerRequest(process, result) ) {
                unique_lock ul(m_RealLoadMtx);
                AKnapSack sack = make_shared<CKnapSack>(process.first, result);
                for (size_t i = 0; i <= result.size(); ++i)
                    m_RealLoad.emplace_back(make_pair(sack, i));
                m_RealLoadEmpty.notify_all();
            }

            prUl.lock();
        }
    }

    // === SHIP LOADING
    pair<AKnapSack, int> removeShipCargo () {
        pair<AKnapSack, int> work;
        unique_lock ul ( m_RealLoadMtx );
        m_RealLoadEmpty.wait(ul, [ this ] () { return !m_RealLoad.empty(); } );
        work = m_RealLoad.front();
        if ( work.first )
            m_RealLoad.pop_front();
        return work;
    }

    void WorkerThread () {
        unique_lock<mutex> ul (m_RealLoadMtx);

        while ( true ) {
            ul.unlock();
            pair<AKnapSack, int> work = removeShipCargo();

            // end signal
            if ( !work.first )
                break;

            vector<CCargo> realLoad; int result;
            if ( work.first->Compute(work.second, result, realLoad) )
                work.first->m_Ship->Load(realLoad);

            ul.lock();
        }
    }

public:

    static int SeqSolver ( const vector<CCargo> & cargo, int maxWeight, int maxVolume, vector<CCargo> & load ) {
        CKnapSack sack ( make_shared<DummyShip> ( maxWeight, maxVolume ), cargo );
        int result;
        for ( size_t i = 0; i < cargo.size(); i++ )
            sack.Compute(i, result, load);
        sack.Compute(cargo.size(), result, load);
        return result;
    }

    void Start ( int customers, int workers ) {
        for ( int i = 0; i < customers; ++i )
            m_Customers.emplace_back( thread(&CCargoPlanner::CustomerThread, this) );
        for ( int i = 0; i < workers; ++i )
            m_Workers.emplace_back( thread(&CCargoPlanner::WorkerThread, this) );
    }
    void Stop () {
        Ship (nullptr);
        for ( auto & thr : m_Customers )
            thr.join();

        {
            unique_lock ul ( m_RealLoadMtx );
            m_RealLoad.emplace_back( make_pair(nullptr, 0) );
        }
        m_RealLoadEmpty.notify_all();

        for ( auto & thr : m_Workers )
            thr.join();
    }

    void Customer ( const ACustomer & customer ) {
        m_People.emplace_back(customer);
    }
    void Ship ( const AShip & ship ) {
        unique_lock prUl ( m_ProcessMtx );
        for ( const auto & person : m_People )
            m_Process.emplace_back(make_pair(ship, person));
        m_ProcessEmpty.notify_all(); // wake up sleeping customers
    }

};

//-------------------------------------------------------------------------------------------------
#ifndef __PROGTEST__
int main (){
    CCargoPlanner  test;
    vector<AShipTest> ships;
    vector<ACustomerTest> customers { make_shared<CCustomerTest> (), make_shared<CCustomerTest> (), make_shared<CCustomerTest> (), make_shared<CCustomerTest> (), make_shared<CCustomerTest> (), make_shared<CCustomerTest> (), make_shared<CCustomerTest> (), make_shared<CCustomerTest> (), make_shared<CCustomerTest> () };

    ships . push_back ( g_TestExtra[0] . PrepareTest ( "New York", customers ) );
    ships . push_back ( g_TestExtra[1] . PrepareTest ( "Barcelona", customers ) );
    ships . push_back ( g_TestExtra[2] . PrepareTest ( "Kobe", customers ) );
    ships . push_back ( g_TestExtra[8] . PrepareTest ( "Perth", customers ) );

    for ( const auto & x : customers )
        test . Customer ( x );

    test . Start ( 2, 6 );

    for ( const auto & x : ships )
    test . Ship ( x );

    test . Stop  ();

    for ( const auto & x : ships )
        cout << x -> Destination () << ": " << ( x -> Validate () ? "ok" : "fail" ) << endl;
    return 0;
}
#endif /* __PROGTEST__ */ 
