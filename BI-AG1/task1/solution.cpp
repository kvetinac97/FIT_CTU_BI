#include <algorithm>
#include <iostream>
#include <map>
#include <queue>
#include <stack>
#include <string>
#include <vector>

using namespace std;

struct Status {
    
    bool m_Infected = false;
    int m_Position = -1;
    
    bool m_Infection = false;
    bool m_Cure = false;
    
    int m_Survival = 0;
    int m_Previous = -1;
    
};

struct Node {
    
    bool m_Toxic = false;
    bool m_Cured = false;
    
    vector<int> m_Next;
    
    Status m_Status;
    Status m_StatusInfected;
    
    bool m_HasStatus = false;
    bool m_HasStatusInfected = false;
    
};

int main ( void ) {
    
    // V = number of planets, E = number of paths
    // A = start planet, B = end planet, C = toxic survival rate
    int v, e, a, b, c;
    cin >> v >> e >> a >> b >> c;
    
    Node * planets = new Node [v];
        
    // Toxic planets
    int toxicCnt, j;
    cin >> toxicCnt;
    
    for ( int i = 0; i < toxicCnt; i++ ) {
        cin >> j;
        planets[j].m_Toxic = true;
    }
    
    // Cure planets
    int cureCnt, k;
    cin >> cureCnt;
    
    for ( int i = 0; i < cureCnt; i++ ) {
        cin >> k;
        planets[k].m_Cured = true;
    }
    
    for ( int i = 0; i < e; i++ ) {
        cin >> j >> k;
        planets[j].m_Next.push_back ( k );
        planets[k].m_Next.push_back ( j );
    }
    
    Status s;
    s.m_Survival = c;
    s.m_Position = a;
    planets[a].m_Status = s;
    planets[a].m_HasStatus = true;
    
    // We need to find path
    queue<Status> visit;
    visit.push ( s );
    
    while ( !visit.empty() ) {
        
        Status s = visit.front();
        visit.pop();

        int position = s.m_Position;
                
        // Died
        if ( s.m_Infected && --s.m_Survival < 0 )
            continue;
        
        // Cured
        if ( s.m_Infected && planets[position].m_Cured ) {
            s.m_Infected = false;
            s.m_Cure = true;
            s.m_Survival = c;
        }
        else
            s.m_Cure = false;
        
        // Infection
        if ( !s.m_Infected && planets[position].m_Toxic ) {
            s.m_Infected = true;
            s.m_Infection = true;
        }
        else
            s.m_Infection = false;
        
        // End
        if ( position == b ) {
            
            vector<int> history;
            
            int pos = position;
            
            do {
                bool infected = s.m_Cure ? true : (s.m_Infection ? false : s.m_Infected);
                s = infected ? planets[pos].m_StatusInfected : planets[pos].m_Status;

                history.push_back ( pos );
                pos = s.m_Previous;
            }
            while ( pos >= 0 );
            
            for ( auto it = history.rbegin(); it != history.rend() - 1; it++ )
                cout << *it << " ";
            cout << position << endl;
            
            return 0;
        }
        
        // Mark as visited
        s.m_Previous = position;

        // Visit children
        for ( size_t i = 0; i < planets[position].m_Next.size(); i++ ) {
            int x = planets[position].m_Next[i];
            s.m_Position = x;
            
            if ( !planets[x].m_HasStatus && ( !planets[x].m_HasStatusInfected || planets[x].m_StatusInfected.m_Survival < s.m_Survival ) && s.m_Infected ) {
                planets[x].m_StatusInfected = s;
                planets[x].m_HasStatusInfected = true;
                visit.push ( s );
            }
            if ( !planets[x].m_HasStatus && !s.m_Infected ) {
                planets[x].m_Status = s;
                planets[x].m_HasStatus = true;
                planets[x].m_HasStatusInfected = true;
                visit.push ( s );
            }
        }
        
    }
    
    cout << "-1" << endl;
    return 0;
    
}
