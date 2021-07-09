#include <iostream>
#include <vector>
#include <stack>
#include <set>

using namespace std;

struct TNode {
    
    TNode * m_RightChildren = nullptr;
    TNode * m_LeftVoisin = nullptr;
    TNode * m_Parent = nullptr;
    
    int m_Value;
    int m_Count = 1;
    
    TNode * Merge ( TNode * other ) {
        return ( other->m_Value < m_Value ) ?
            other->AddNode ( this ) : AddNode ( other );
    }
    
    void Print () {
        for ( TNode * node = this; node; node = node->m_RightChildren ) {
            cout << node->m_Value << " ";
            for ( TNode * hm = node->m_LeftVoisin; hm; hm = hm->m_LeftVoisin )
                cout << hm->m_Value << " ";
            cout << endl;
        }
    }
    
private:
    
    TNode * AddNode ( TNode * child ) {
        child->m_Parent = this;
        child->m_LeftVoisin = m_RightChildren;
        m_RightChildren = child;
        m_Count += child->m_Count;
        return this;
    }
    
};

class CBinomialHeap {
    
    TNode ** m_Trees = nullptr;
    int m_TreeCnt = 0;
    TNode * m_Minimum = nullptr;
    
public:
    
    CBinomialHeap ( int cnt = 1 ) {
        m_Trees = new TNode * [cnt];
        m_TreeCnt = 0;
        
        for ( int i = 0; i < cnt; i++ )
            m_Trees[i] = nullptr;
    }
    ~CBinomialHeap() {
        delete [] m_Trees;
        m_Trees = nullptr;
        m_TreeCnt = 0;
    }
    
    void Add ( int value ) {
        // Empty heap
        if ( m_TreeCnt == 0 ) {
            TNode * node = new TNode;
            node->m_Value = value;
            
            m_Trees[0] = node;
            m_Minimum = node;
            m_TreeCnt = 1;
            return;
        }
        
        // Merge
        CBinomialHeap other;
        other.Add ( value );
        Merge ( other );
    }
    
    void Merge ( const CBinomialHeap & other ) {
        // Start it
        CBinomialHeap result ( m_TreeCnt > other.m_TreeCnt ? m_TreeCnt + 1 : other.m_TreeCnt + 1 );
        int level = 0; TNode * carry = nullptr;
        
        while ( carry || level < m_TreeCnt || level < other.m_TreeCnt ) {
            
            // Both set
            if ( ( level < m_TreeCnt && m_Trees[level] ) && ( level < other.m_TreeCnt && other.m_Trees[level] ) ) {
                result.m_Trees[level] = carry;
                carry = m_Trees[level]->Merge( other.m_Trees[level] );

                ++level;
                continue;
            }
            
            // Both empty
            if ( ( level >= m_TreeCnt || !m_Trees[level] ) && ( level >= other.m_TreeCnt || !other.m_Trees[level] ) ) {
                result.m_Trees[level] = carry;
                carry = nullptr;
                ++level;
                continue;
            }
            
            TNode * node = ( level >= m_TreeCnt || !m_Trees[level] ) ? other.m_Trees[level] : m_Trees[level];
            
            if ( carry ) {
                result.m_Trees[level] = nullptr;
                carry = node->Merge ( carry );
                
                ++level;
                continue;
            }
            
            result.m_Trees[level] = node;
            ++level;
            
        }
        result.m_TreeCnt = level;
        
        if ( m_Minimum && !other.m_Minimum )
            result.m_Minimum = m_Minimum;
        else if ( !m_Minimum && other.m_Minimum )
            result.m_Minimum = other.m_Minimum;
        else
            result.m_Minimum = m_Minimum->m_Value < other.m_Minimum->m_Value ? m_Minimum : other.m_Minimum;
        
        delete [] m_Trees;
        m_Trees = result.m_Trees;
        m_TreeCnt = result.m_TreeCnt;
        m_Minimum = result.m_Minimum;
        result.m_Trees = nullptr;
    }
    
    int RemoveMinimum () {
        int val = m_Minimum->m_Value;
        
        TNode * savepoint = m_Minimum->m_RightChildren;
        int childCount = 0, minPos = -1;
        
        for ( int i = 0; i < m_TreeCnt; i++ )
            if ( m_Trees[i] == m_Minimum ) {
                minPos = i;
                break;
            }
        
        for ( TNode * node = savepoint; node; node = node->m_LeftVoisin ) {
            node->m_Parent = nullptr;
            childCount++;
        }
        
        CBinomialHeap tmp ( childCount );
        int i = 0;
        
        for ( TNode * node = savepoint; node; node = node->m_LeftVoisin ) {
            tmp.m_Trees[i++] = node;
        }
        tmp.m_TreeCnt = i;
        
        m_Trees[minPos] = nullptr;
        Merge ( tmp );
        
        m_Minimum = m_Trees[0];
        for ( int i = 1; i < m_TreeCnt; i++ ) {
            if ( !m_Minimum || ( m_Trees[i] && m_Trees[i]->m_Value < m_Minimum->m_Value ) )
                m_Minimum = m_Trees[i];
        }
        
        return val;
    }
    
    void Print () {
        
        cout << "Minimum of heap: " << m_Minimum->m_Value << endl;
        
        stack<TNode *> nodes;
        set<TNode *> visited;
        
        for ( int level = 0; level < m_TreeCnt; level++ )
            if ( m_Trees[level] )
                nodes.push ( m_Trees[level] );
        
        while ( !nodes.empty() ) {
            TNode * node = nodes.top();
            nodes.pop();
            
            if ( !node )
                continue;
            
            visited.insert ( node );
            nodes.push ( node->m_LeftVoisin );
            nodes.push ( node->m_RightChildren );
        }
        
        for ( auto it = visited.begin(); it != visited.end(); ++it )
            cout << (*it)->m_Value << endl;
        
        
    }
    
};



int main ( void ) {
    cout << "HI!" << endl;
    
    vector<int> hm = {1, 2, 3, 4, 5, 128};
    vector<int> hmheyo = {10, -2, 30, -4, 50, 28, -15, 3, 121};
    
    CBinomialHeap heap1, heap2;
    for ( int h : hm )
        heap1.Add ( h );
    
    for ( int h : hmheyo )
        heap2.Add ( h );
    
    heap1.Merge ( heap2 );
    
    for ( int i = 0; i < 15; i++ )
        cout << heap1.RemoveMinimum() << endl;
    
    return 0;
}
