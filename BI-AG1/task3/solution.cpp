#include <cmath>
#include <iostream>
#include <vector>
#include <map>

using namespace std;

const int MAX_INFECTED = 1073741824;
const int STATE_DEFAULT = 0;
const int STATE_ACTIVE = 1;
const int STATE_DISABLED = 2;

struct CRestriction {
    
    int m_PopularityMakeActive;
    int m_PopularityActive;
    int m_PopularityDestroy;
    
    double m_Coeficient;
    
    CRestriction () = default;
    CRestriction ( int p, int m, int n, double c )
    : m_PopularityMakeActive ( p ), m_PopularityActive ( m ), m_PopularityDestroy ( n ),
    m_Coeficient ( c ) {};
    
};

struct CKey {
    
    CRestriction * m_Restrictions;
    vector<int> m_States;
    int m_Popularity;
    
    CKey () = default;
    CKey ( CRestriction * & r, int rc, int pop ) : m_Restrictions ( r ), m_Popularity ( pop ) {
        for ( int i = 0; i < rc; ++i )
        m_States.push_back ( STATE_DEFAULT );
    }
    
    int getActive () {
        int active = 0;
        for ( size_t i = 0; i < m_States.size(); ++i )
        if ( m_States[i] == STATE_ACTIVE )
            active++;
        return active;
    }
    
    friend bool operator < ( const CKey & left, const CKey & right ) {
        if ( left.m_Popularity == right.m_Popularity )
            return lexicographical_compare ( left.m_States.begin(), left.m_States.end(), right.m_States.begin(), right.m_States.end() );
        return left.m_Popularity < right.m_Popularity;
    }
    
};

struct CChange {
    
    int m_Week;
    bool m_Create;
    int m_Number;
    
    CChange () = default;
    CChange ( int w, bool c, int n )
    : m_Week ( w ), m_Create ( c ), m_Number ( n ) {}
    
};

struct CValue {
    
    int m_Infected;
    const CKey * m_Previous;
    CChange m_Change;
    
    CValue () = default;
    CValue ( int i, const CKey * prev, CChange chg )
    : m_Infected ( i ), m_Previous ( prev ), m_Change ( chg ) {}
    
};

int getPopularityActive ( int popularity, const CKey & value ) {
    int pop = popularity;
    for ( size_t i = 0; i < value.m_States.size(); ++i )
    if ( value.m_States[i] == STATE_ACTIVE )
        pop += value.m_Restrictions[i].m_PopularityActive;
    
    return min(pop, 100);
}

int getInfected ( int infected, double r, const CKey & value ) {
    double rep = r;
    for ( size_t i = 0; i < value.m_States.size(); ++i )
    if ( value.m_States[i] == STATE_ACTIVE )
        rep *= value.m_Restrictions[i].m_Coeficient;
    
    if ( (0.00000001 + infected * rep) > MAX_INFECTED + 1 ) // check
        return -1;
    
    return floor ( 0.00000001 + infected * rep );
}

void loadInput ( int & weeks, int & maxActive, int & rc, int & ec, int & sp, int & si, double & r, CRestriction * & rs, int * & elections ) {
    
    // Load all
    cin >> weeks >> maxActive >> rc >> ec >> sp >> si >> r;
    
    // Init restrictions
    rs = new CRestriction [ rc ];
    for ( int j = 0; j < rc; j++ ) {
        int rp, rm, rn; double dr;
        cin >> rp >> rm >> rn >> dr;
        rs[j] = CRestriction ( rp, rm, rn, dr );
    }
    
    // Init elections
    elections = new int [ weeks + 1 ];
    for ( int i = 0; i <= weeks; i++ )
        elections[i] = 0;
    for ( int i = 0; i < ec; i++ ) {
        int key, value;
        cin >> key >> value;
        elections[key] = value;
    }
    
}

void fillTable ( map<CKey, CValue> * & T, int weeks, int ma, int rc, int sp, int si, double r, CRestriction * rs, int * elections ) {
    // Start
    T[1][CKey(rs, rc, sp)] = CValue(si, nullptr, CChange());
    
    // Each week, fill it up
    for ( int week = 1; week <= weeks; ++week  ) {
        for ( auto it = T[week - 1].begin(); it != T[week - 1].end(); ++it ) {
            // Last week value
            CKey key = it->first;
            CValue value = it->second;
            
            // Do nothing
            CKey key2 = key;
            key2.m_Popularity = getPopularityActive ( key.m_Popularity, key );
            
            auto nothingIt = T[week].find ( key2 );
            CValue value2 ( getInfected ( value.m_Infected, r, key ), &it->first, CChange() );
            
            if ( ( nothingIt == T[week].end() || value2.m_Infected < nothingIt->second.m_Infected ) && value2.m_Infected != -1 && key2.m_Popularity >= elections[week] )
                T[week][key2] = value2;
            
            // Activate or disable one restriction
            for ( size_t i = 0; i < key.m_States.size(); ++i ) {
                if ( ( key.m_States[i] == STATE_DEFAULT && key.getActive() < ma ) || key.m_States[i] == STATE_ACTIVE ) {
                    // Activate this state
                    CKey keyX = key;
                    keyX.m_States[i] = key.m_States[i] == STATE_DEFAULT ? STATE_ACTIVE : STATE_DISABLED;
                    keyX.m_Popularity = getPopularityActive ( key.m_Popularity + ( key.m_States[i] == STATE_DEFAULT ? rs[i].m_PopularityMakeActive : rs[i].m_PopularityDestroy - rs[i].m_PopularityActive ), key );
                    
                    auto changeIt = T[week].find ( keyX );
                    CValue valueX ( getInfected ( value.m_Infected, r, keyX ), &it->first, CChange(week, key.m_States[i] == STATE_DEFAULT, i) );
                    
                    if ( ( changeIt == T[week].end() || changeIt->second.m_Infected >= valueX.m_Infected ) && valueX.m_Infected != -1 && keyX.m_Popularity >= elections[week] )
                        T[week][keyX] = valueX;
                }
            }
        }
    }
}

void printResult ( map<CKey, CValue> * & T, int weeks ) {
    // No solution
    if ( T[weeks].size() == 0 ) {
        cout << "-1" << endl;
        return;
    }
    
    // Init
    int min = MAX_INFECTED + 1;
    CValue minValue;
    CKey minKey;
    
    // Find best
    for ( auto it = T[weeks].begin(); it != T[weeks].end(); ++it ) {
        if ( it->second.m_Infected < min ) {
            min = it->second.m_Infected;
            minValue = it->second;
            minKey = it->first;
        }
    }
    
    // Print
    int wk = weeks, cnt = 0;
    map<int, CChange> changes;
    
    while ( wk > 0 ) {
        // Print value
        minValue = T[wk][minKey];
        
        // Save it
        if ( minValue.m_Change.m_Week > 0 ) {
            changes[minValue.m_Change.m_Week] = minValue.m_Change;
            cnt++;
        }
        
        // End
        if ( !minValue.m_Previous )
            break;
        
        --wk;
        minKey = *(minValue.m_Previous);
    }
    
    // {changes} {minimumInfected}
    cout << cnt << " " << min << endl;
    for ( auto it = changes.begin(); it != changes.end(); ++it )
        cout << it->first << " " << ( it->second.m_Create ? "zavest" : "zrusit" ) << " " << it->second.m_Number << endl;
}

int main ( void ) {
    
    // Init
    int inputCount;
    cin >> inputCount;
    
    // Load each input
    for ( int i = 0; i < inputCount; i++ ) {
        // Init general
        int weeks, maxActive, rc, electionCount, startPopularity, startInfected, * elections;
        double r; CRestriction * rs;
        loadInput ( weeks, maxActive, rc, electionCount, startPopularity, startInfected, r, rs, elections );
        
        // Create table
        map<CKey, CValue> * T = new map<CKey, CValue> [ weeks + 1 ];
        fillTable ( T, weeks, maxActive, rc, startPopularity, startInfected, r, rs, elections );
        
        // Free memory
        delete [] elections;
        delete [] rs;
        
        // Print result and free
        printResult ( T, weeks );
        delete [] T;
    }
    
    return 0;
    
}
