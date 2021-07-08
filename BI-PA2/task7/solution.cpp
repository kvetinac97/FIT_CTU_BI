#ifndef __PROGTEST__

#include <cassert>
#include <cctype>
#include <iostream>
#include <set>
#include <forward_list>
#include <map>
#include <vector>
#include <string>

using namespace std;

class CMock {

    string m_Str;

public:

    CMock () = delete;
    CMock ( const char * str ) : m_Str ( string(str) ) {}

    CMock ( const CMock & src ) : m_Str ( src.m_Str ) {}
    CMock & operator = ( const CMock & src ) { m_Str = src.m_Str; return *this; }
    ~CMock () {}

    friend bool operator == ( const CMock & l, const CMock & r ) {
        return l.m_Str == r.m_Str;
    }
    friend bool operator != ( const CMock & l, const CMock & r ) {
        return l.m_Str != r.m_Str;
    }

};
#endif /* __PROGTEST__ */


template <typename _Type, typename _Comparator = equal_to<typename _Type::value_type> >
struct CNode {

    _Type                                m_Val;
    _Comparator                   m_Comparator;
    CNode<_Type, _Comparator> *         m_Fail;
    vector<CNode<_Type, _Comparator> *> m_Next;

    bool          m_Final = false;
    set<int>       m_Codes;

    int m_Depth = 0;

    CNode ( const _Type & val, const _Comparator & comparator, CNode * fail )
            : m_Val ( val ), m_Comparator ( comparator ), m_Fail ( fail ) {}

    bool contains ( const _Type & val, CNode<_Type, _Comparator> * & res ) {
        for ( CNode<_Type, _Comparator> * next : m_Next )
            if ( m_Comparator ( val, next->m_Val ) ) {
                res = next;
                return true;
            }
        return false;
    }

    CNode<_Type, _Comparator> * m_RootTmp;

    void generateFailLink ( CNode<_Type, _Comparator> * node, CNode <_Type, _Comparator> * failFrom = nullptr ) {
        if ( failFrom && failFrom->contains ( node->m_Val, failFrom ) )
            node->m_Fail = failFrom;
        else if ( m_RootTmp->contains ( node->m_Val, failFrom ) )
            node->m_Fail = failFrom;
        else
            node->m_Fail = nullptr;
    }

    void generateFailLinks ( CNode<_Type, _Comparator> * node, CNode<_Type, _Comparator> * failFrom = nullptr ) {
        generateFailLink ( node, failFrom );
        for ( CNode<_Type, _Comparator> * node2 : node->m_Next )
            generateFailLinks ( node2, node->m_Fail );
    }

    void createFailLinks ( CNode<_Type, _Comparator> * root ) {
        m_RootTmp = root;
        for ( CNode<_Type, _Comparator> * rootChild : root->m_Next ) {
            rootChild->m_Fail = nullptr;
            for ( CNode<_Type, _Comparator> * node : rootChild->m_Next )
                generateFailLinks ( node, rootChild->m_Fail );
        }
    }

    void destroy () {
        for ( CNode<_Type, _Comparator> * child : m_Next )
            child->destroy();
        delete this;
    }

};

template <typename _Type, typename _Comparator = equal_to<typename _Type::value_type> >
class CSearch {

    map<int, _Type> m_Needles;
    mutable CNode<typename _Type::value_type, _Comparator> * m_Root = nullptr;
    mutable bool m_TreeValid = false;
    _Comparator m_Comparator;

    void generateTree () const {
        // Step 1: create tree
        for ( auto it : m_Needles ) {
            CNode<typename _Type::value_type, _Comparator> * pos = m_Root;

            for ( auto ch = it.second.begin(); ch != it.second.end(); ch++ ) {

                // First char for first time
                if ( !m_Root ) {
                    m_Root = new CNode<typename _Type::value_type, _Comparator> ( *ch, m_Comparator, nullptr ); // could be empty
                    pos = new CNode<typename _Type::value_type, _Comparator> ( *ch, m_Comparator, nullptr );
                    m_Root->m_Next.push_back ( pos );
                    continue;
                }

                // Already contained? Move
                if ( pos->contains ( *ch, pos ) )
                    continue;

                // Otherwise, add and move
                CNode <typename _Type::value_type, _Comparator> * tmp = new CNode<typename _Type::value_type, _Comparator> ( *ch, m_Comparator, nullptr );
                pos->m_Next.push_back ( tmp );
                pos = tmp;

            }

            pos->m_Final = true;
            pos->m_Codes.insert ( it.first );
        }

        // Step two: create fail links
        m_Root->createFailLinks ( m_Root );
        m_TreeValid = true;
    }

public:

    CSearch ( _Comparator comparator = equal_to<typename _Type::value_type>() )
            : m_Comparator ( comparator ) {}
    ~CSearch () { m_Root->destroy(); m_Root = nullptr; }
    CSearch ( const CSearch & src ) = delete;
    CSearch & operator = ( const CSearch & src ) = delete;

    void Add ( int id, const _Type & needle ) {
        m_Needles.insert ( { id, needle } );
        m_TreeValid = false;

        if ( m_Root ) {
            m_Root->destroy();
            m_Root = nullptr;
        }
    }

    set<int> Search ( const _Type & hayHeap ) const {

        if ( !m_TreeValid )
            generateTree ();

        set<int> result;

        // Step three: actually find it
        CNode <typename _Type::value_type, _Comparator> * node = m_Root;
        for ( auto it = hayHeap.begin(); it != hayHeap.end(); ) {

            // Find, move in tree
            if ( node->contains( *it, node ) ) {
                // Found code, add to result
                CNode<typename _Type::value_type, _Comparator> * tempo = node;
                while ( node->m_Final || node->m_Fail ) {
                    if ( node->m_Final ) {
                        for ( int code : node->m_Codes )
                            result.insert ( code );
                    }
                    node = node->m_Fail ? node->m_Fail : m_Root; // jump back
                }
                node = tempo;
                ++it;
            }
            else {

                if ( node == m_Root && !node->m_Fail ) // we would cycle
                    ++it;

                // just move in tree
                node = node->m_Fail ? node->m_Fail : m_Root;
            }

        }

        return result;

    }

};

#ifndef __PROGTEST__
int main ( void ) {

    CSearch <string> test1;
    test1 . Add    ( 0, "hello" );
    test1 . Add    ( 1, "world" );
    test1 . Add    ( 2, "rld" );
    test1 . Add    ( 3, "ell" );
    test1 . Add    ( 4, "hell" );
    assert ( test1 . Search ( "hello world!" ) == (set<int> { 0, 1, 2, 3, 4 }) );
    assert ( test1 . Search ( "hEllo world!" ) == (set<int> { 1, 2 }) );

    CSearch <string, bool (*)(const char &, const char &)> test2 ( [] ( const char & a, const char & b ) { return toupper ( a ) == toupper ( b ); } );
    test2 . Add    ( 0, "hello" );
    test2 . Add    ( 1, "world" );
    test2 . Add    ( 2, "rld" );
    test2 . Add    ( 3, "ell" );
    test2 . Add    ( 4, "hell" );

    assert ( test2 . Search ( "HeLlO WoRlD!" ) == (set<int> { 0, 1, 2, 3, 4 }) );
    assert ( test2 . Search ( "hallo world!" ) == (set<int> { 1, 2 }) );
    assert ( test2 . Search ( "hallo world!" ) == (set<int> { 1, 2 }) );

    CSearch <string> terest;
    terest . Add ( 0, "test" );
    terest . Add ( 1, "terest" );
    terest . Add ( 2, "rest" );
    terest . Add ( 3, "teres" );

    assert ( terest.Search ( "terest" ) == (set<int> { 1, 2, 3 }) );
    assert ( terest.Search ( "teterest" ) == (set<int> { 1, 2, 3 }) );
    assert ( terest.Search ( "tetererest" ) == (set<int> { 2 }) );
    assert ( terest.Search ( "testerest" ) == (set<int> { 0, 1, 2, 3 } ) );

    CSearch <vector<int> > test3;
    test3 . Add    ( 0, { 1, 6, 1, 6, 9, 12 } );
    test3 . Add    ( 1, { 9, 12, 7 } );
    assert ( test3 . Search ( vector<int> { 1, 6, 1, 6, 1, 6, 9, 12, 8 } ) == (set<int> { 0 }) );
    assert ( test3 . Search ( vector<int> { 1, 1, 6, 1, 6, 8, 12, 8 } ) == (set<int> {}) );

    CSearch <string> testx;

    testx.Add(5, "a");
    testx.Add(7, "ab");
    testx.Add(10, "bab");
    testx.Add(12, "bc");
    testx.Add(15, "bca");
    testx.Add(18, "c");
    testx.Add(20, "caa");
    assert ( testx.Search ( "babacbacabcabacbacbac" ) == (set<int> { 10, 7, 5, 12, 15, 18 }) );
    testx.Add(22, "caa");
    assert ( testx.Search ( "caa" ) == (set<int> { 5, 18, 20, 22 }) );

    assert ( testx.Search("a") == (set<int> { 5 }) );
    assert ( testx.Search("ab") == (set<int> { 5, 7 }) );
    assert ( testx.Search("bab") == (set<int> { 5, 7, 10 }) );

    assert ( testx.Search("b") == (set<int> {}) );
    assert ( testx.Search("bc") == (set<int> { 12, 18 }) );
    assert ( testx.Search("bca") == (set<int> { 5, 18, 12, 15 }) );
    assert ( testx.Search("bcaa") == (set<int> { 5, 18, 12, 15, 20, 22 }) );

    CSearch<string> testl;

    testl.Add(1, "his");
    testl.Add(2, "hers");
    testl.Add(3, "he");
    testl.Add(4, "she");

    assert ( testl.Search("ahishers") == (set<int> { 1, 2, 3, 4 }) );

    CSearch <vector<string> > test4;
    test4 . Add    ( 0, { "Prague", "Bern", "Rome" } );
    test4 . Add    ( 1, { "London", "Prague", "Bern" } );
    test4 . Add    ( 2, { "London", "Rome" } );
    assert ( test4 . Search ( vector<string> { "Berlin", "London", "Prague", "Bern", "Rome", "Moscow" } ) == (set<int> { 0, 1 }) );

    CSearch <vector<CMock> > test5;
    test5 . Add    ( 0, { "Prague", "Bern", "Rome" } );
    test5 . Add    ( 1, { "London", "Prague", "Bern" } );
    test5 . Add    ( 2, { "London", "Rome" } );
    assert ( test5 . Search ( vector<CMock> { "Berlin", "London", "Prague", "Bern", "Rome", "Moscow" } ) == (set<int> { 0, 1 }) );

    CSearch <forward_list<int> > test6;
    test6 . Add    ( 0, { 1, 6, 1, 6, 9, 12 } );
    test6 . Add    ( 1, { 9, 12, 7 } );
    assert ( test6 . Search ( forward_list<int> { 1, 6, 1, 6, 1, 6, 9, 12, 8 } ) == (set<int> { 0 }) );

    return 0;

}
#endif /* __PROGTEST__ */
