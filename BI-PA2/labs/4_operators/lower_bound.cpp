#include <iostream>
#include <sstream>
#include <string>
#include <vector>

using namespace std;

struct TItem {
    string m_ID;
    int m_Count;

    TItem ( string id, int count )
    : m_ID ( id ), m_Count (count) {}

    friend bool operator == ( const TItem & l, const TItem & r ) {
        return l.m_ID == r.m_ID;
    }

    friend bool operator < ( const TItem & l, const TItem & r ) {
        return l.m_ID < r.m_ID;
    }

};

int main () {

    string line = "+5 TEST";
    vector<TItem> items;

    while ( (bool) getline(cin, line) ) {

        istringstream str ( line );

        char type;
        int count;
        string id;

        if ( !(str >> type >> count >> id) || (type != '+' && type != '-') )
            break;

        TItem item ( id, count );
        auto f = lower_bound ( items.begin(), items.end(), item );
        bool found = !( f == items.end() || (*f).m_ID != item.m_ID );

        if ( type == '+' ) {
            int cnt;
            if ( found ) {
                f->m_Count += count;
                cnt = f->m_Count;
            }
            else {
                items.insert(f, item);
                cnt = item.m_Count;
            }

            cout << "Status: " << cnt << endl;
        }

        if ( type == '-' ) {
            int cnt;

            if ( !found ) {
                cout << "Not found." << endl;
                cnt = 0;
            }
            else if ( f->m_Count < count ) {
                cout << "Not enough." << endl;
                cnt = f->m_Count;
            }
            else if ( f->m_Count == count ) {
                items.erase(f);
                cnt = 0;
            }
            else {
                f->m_Count -= count;
                cnt = f->m_Count;
            }

            cout << "Status: " << cnt << endl;
        }

    }

    if ( !items.empty() ) {
        cout << "Is not empty" << endl;
        return 2;
    }

    cout << "Ended with success" << endl;
    return 0;

}
