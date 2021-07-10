#ifndef __PROGTEST__
#include <algorithm>
#include <cassert>
#include <cstdio>
#include <cstdlib>
#include <deque>
#include <iostream>
#include <list>
#include <map>
#include <memory>
#include <numeric>
#include <optional>
#include <queue>
#include <set>
#include <sstream>
#include <stack>
#include <vector>
using namespace std;

using State = unsigned int;
using Symbol = char;

struct MISNFA {
	std::set < State > m_States;
	std::set < Symbol > m_Alphabet;
	std::map < std::pair < State, Symbol >, std::set < State > > m_Transitions;
	std::set < State > m_InitialStates;
	std::set < State > m_FinalStates;
};

struct DFA {
	std::set < State > m_States;
	std::set < Symbol > m_Alphabet;
	std::map < std::pair < State, Symbol >, State > m_Transitions;
	State m_InitialState;
	std::set < State > m_FinalStates;

	bool operator== ( const DFA & other ) {
        return
		std::tie ( m_States, m_Alphabet, m_Transitions, m_InitialState, m_FinalStates ) ==
		std::tie ( other.m_States, other.m_Alphabet, other.m_Transitions, other.m_InitialState, other.m_FinalStates );
	}
    
    friend ostream & operator << ( ostream & os, const DFA & dfa ) {
        os << "States:" << endl;
        for ( State s : dfa.m_States )
            os << s << endl;
        os << "Alphabet:" << endl;
        for ( Symbol s : dfa.m_Alphabet )
            os << s << endl;
        os << "Delta:" << endl;
        for ( auto it = dfa.m_Transitions.begin(); it != dfa.m_Transitions.end(); it++ ) {
            os << "5(" << it->first.first << "," << it->first.second << ") = " << it->second <<endl;
        }
        os << "Initial:" << endl;
        os << dfa.m_InitialState << endl;
        os << "Final:" << endl;
        for ( State s : dfa.m_FinalStates )
            os << s << endl;
        return os;
    }
};
#endif

DFA determinize ( const MISNFA & nfa ) {
    
    DFA dfa;
    dfa.m_Alphabet = nfa.m_Alphabet;
    State st = 0;
    
    queue<State> states;
    map<set<State>, State> mapper;
    map<State, set<State>> mapper2;
    
    states.push ( st );
    dfa.m_States.insert ( st );
    dfa.m_InitialState = st;
    mapper.insert ( {nfa.m_InitialStates, st} );
    mapper2.insert ( {st++, nfa.m_InitialStates} );
        
    while ( !states.empty() ) {
        State state = states.front();
        states.pop();

        for ( auto a : nfa.m_Alphabet ) {
            set<State> innerstates;
            for ( State s : mapper2[state] ) {
                pair<State, Symbol> key = {s, a};
                auto it = nfa.m_Transitions.find(key);
                if ( it != nfa.m_Transitions.end() )
                    for ( State ss : it->second )
                        innerstates.insert ( ss );
            }

            auto it = mapper.find(innerstates);
            State saveState = ( it == mapper.end() ? st : it->second );
            if ( it == mapper.end() ) {
                mapper2.insert ( {st, innerstates} );
                mapper.insert ( {innerstates, st} );
                states.push ( st );
                dfa.m_States.insert ( st++ );
            }
                
            pair<State, Symbol> key = {state, a};
            dfa.m_Transitions[key] = saveState;
        }
    }
    
    for ( auto it = mapper2.begin(); it != mapper2.end(); it++ )
        for ( State q : it->second ) {
            if ( nfa.m_FinalStates.find(q) != nfa.m_FinalStates.end() ) {
                dfa.m_FinalStates.insert ( it->first );
                break;
            }
        }
    
    return dfa;
    
}

DFA trim ( const DFA & dfa ) {

    DFA dfa2 = dfa;
    set<State> unreachable = dfa.m_States;
    set<State> pointless = dfa.m_States;
    pointless.erase ( dfa.m_InitialState );

    // DFS od konce
    for ( State s : dfa.m_FinalStates ) {
        
        stack<State> q;
        map<State, bool> visited;
        q.push ( s );
        pointless.erase ( s );
        
        while ( !q.empty() ) {
            State state = q.top();
            q.pop();
            
            if ( visited.find(state) != visited.end() )
                continue;
            
            visited[state] = true;
            for ( auto it = dfa.m_Transitions.begin(); it != dfa.m_Transitions.end(); it++ ) {
                if ( it->second == state ) {
                    State previous = it->first.first;
                    pointless.erase ( previous );
                    q.push ( previous );
                }
            }
            
        }
        
    }
    
    // DFS od začátku
    stack<State> q;
    map<State, bool> visited;
    q.push ( dfa.m_InitialState );
    unreachable.erase ( dfa.m_InitialState );
    
    while ( !q.empty() ) {
        State state = q.top();
        q.pop();
        
        if ( visited.find(state) != visited.end() )
            continue;
        
        visited[state] = true;
        for ( Symbol a : dfa.m_Alphabet ) {
            pair<State, Symbol> key = {state, a};
            if ( dfa.m_Transitions.find(key) == dfa.m_Transitions.end() )
                continue;

            State next = dfa.m_Transitions.at(key);
            unreachable.erase ( next );
            q.push ( next );
        }
    }
    
    for ( State s : unreachable ) {
        for ( auto it = dfa.m_Transitions.begin(); it != dfa.m_Transitions.end(); it++ ) {
            if ( it->second == s || it->first.first == s )
                dfa2.m_Transitions.erase ( it->first );
        }
        dfa2.m_States.erase ( s );
        dfa2.m_FinalStates.erase ( s );
    }
    for ( State s : pointless ) {
        for ( auto it = dfa.m_Transitions.begin(); it != dfa.m_Transitions.end(); it++ ) {
            if ( it->second == s || it->first.first == s )
                dfa2.m_Transitions.erase ( it->first );
        }
        dfa2.m_States.erase ( s );
        dfa2.m_FinalStates.erase ( s );
    }

    return dfa2;
    
}

DFA minimize ( const DFA & dfa ) {
    
    DFA dfa2 = dfa;
    
    map<State, State> equivalence;
    map<pair<State, map<Symbol, State>>, set<State>> behaviours;
    
    // eqv0
    for ( State s : dfa.m_States ) {
        State v = ( dfa.m_FinalStates.find(s) != dfa.m_FinalStates.end() ? 1 : 0 );
        equivalence.insert ( {s, v} );
    }
    
    int i = 0;
    do {
        // Step 1: set behaviours
        for ( State s : dfa.m_States ) {
            map<Symbol,State> behaviour;
            for ( Symbol a : dfa.m_Alphabet ) {
                pair<State,Symbol> key = {s, a};
                if ( dfa.m_Transitions.find(key) != dfa.m_Transitions.end() )
                    behaviour[a] = equivalence[dfa.m_Transitions.at(key)];
            }
            
            pair<State,map<Symbol,State>> key = {equivalence[s], behaviour};
            if ( behaviours.find(key) != behaviours.end() ) {
                behaviours[key].insert ( s );
            }
            else
                behaviours[key] = { s };
        }
        
        // Step 3: update classes of equivalence
        equivalence.clear();

        State nx = 0;
        for ( auto it = behaviours.begin(); it != behaviours.end(); it++ ) {
            State i = nx++;
            for ( State s : it->second )
                equivalence[s] = i;
        }
        
        behaviours.clear();
    }
    while ( i++ < 1000 );
    
    dfa2.m_InitialState = equivalence[dfa.m_InitialState];
    dfa2.m_States.clear();
    for ( State s : dfa.m_States )
        dfa2.m_States.insert ( equivalence[s] );
    dfa2.m_FinalStates.clear();
    for ( State s : dfa.m_FinalStates )
        dfa2.m_FinalStates.insert ( equivalence[s] );
    dfa2.m_Transitions.clear();
    for ( auto it = dfa.m_Transitions.begin(); it != dfa.m_Transitions.end(); it++ ) {
        pair<State, Symbol> key = { equivalence[it->first.first], it->first.second };
        dfa2.m_Transitions[key] = equivalence[it->second];
    }
        
    if ( dfa2.m_FinalStates.empty() )
        dfa2.m_Transitions.clear();
    
    return dfa2;
}


#ifndef __PROGTEST__

#include "sample.h"

int main ( ) {
	// determinize
	assert ( determinize ( in0 ) == outD0 );
	assert ( determinize ( in1 ) == outD1 );
	assert ( determinize ( in2 ) == outD2 );
	assert ( determinize ( in3 ) == outD3 );
	assert ( determinize ( in4 ) == outD4 );
	assert ( determinize ( in5 ) == outD5 );
	assert ( determinize ( in6 ) == outD6 );
	assert ( determinize ( in7 ) == outD7 );
	assert ( determinize ( in8 ) == outD8 );
	assert ( determinize ( in9 ) == outD9 );
	assert ( determinize ( in10 ) == outD10 );
	assert ( determinize ( in11 ) == outD11 );
	assert ( determinize ( in12 ) == outD12 );
	assert ( determinize ( in13 ) == outD13 );

	// trim
	assert ( trim ( determinize ( in0 ) ) == outT0 );
	assert ( trim ( determinize ( in1 ) ) == outT1 );

	// minimize
    minimize ( trim ( determinize ( xx ) ) );
    assert (  false );
	assert ( minimize ( trim ( determinize ( in0 ) ) ) == outM0 );
    assert ( minimize ( trim ( determinize ( in1 ) ) ) == outM1 );
	assert ( minimize ( trim ( determinize ( in2 ) ) ) == outM2 );
	assert ( minimize ( trim ( determinize ( in3 ) ) ) == outM3 );
	assert ( minimize ( trim ( determinize ( in4 ) ) ) == outM4 );
	assert ( minimize ( trim ( determinize ( in5 ) ) ) == outM5 );
	assert ( minimize ( trim ( determinize ( in6 ) ) ) == outM6 );
	assert ( minimize ( trim ( determinize ( in7 ) ) ) == outM7 );
	assert ( minimize ( trim ( determinize ( in8 ) ) ) == outM8 );
	assert ( minimize ( trim ( determinize ( in9 ) ) ) == outM9 );
	assert ( minimize ( trim ( determinize ( in10 ) ) ) == outM10 );
	assert ( minimize ( trim ( determinize ( in11 ) ) ) == outM11 );
	assert ( minimize ( trim ( determinize ( in12 ) ) ) == outM12 );
	assert ( minimize ( trim ( determinize ( in13 ) ) ) == outM13 );

	return 0;
}
#endif
