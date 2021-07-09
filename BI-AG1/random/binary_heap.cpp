#include <iostream>
#define T int

using namespace std;

class CBinaryHeap {
    
    T *  m_Values;
    size_t m_Size;
    size_t  m_Max;
    
    int LeftSon ( int root ) {
        return root * 2 + 1;
    }
    int RightSon ( int root ) {
        return root * 2 + 2;
    }
    int Parent ( int son ) {
        return (son - 1) / 2;
    }
    
    void Realloc () {
        T * m_Val = new T[m_Max = (m_Max * 2)];
        for ( size_t i = 0; i < m_Size; ++i )
            m_Val[i] = m_Values[i];
        
        delete [] m_Values;
        m_Values = m_Val;
    }
    
    /*
     * Construct binary heap from array
     * complexity O(n)
     */
    void Heapify () {
        // Start with last non-leaf node and continue
        for ( int i = ( m_Size / 2 ) - 1; i >= 0; i-- )
            BubbleDown ( i );
    }
    
    void BubbleDown ( int root = 0 ) {
        // Find whether any son is violating the rules
        int min = root, left = LeftSon(root), right = RightSon(root);
        
        if ( left < m_Size && m_Values[left] < m_Values[min] )
            min = left;
        
        if ( right < m_Size && m_Values[right] < m_Values[min] )
            min = right;
        
        // Violating
        if ( min != root ) {
            swap ( m_Values[min], m_Values[root] );
            BubbleDown ( min ); // fix
        }
    }
    
    void BubbleUp ( int position ) {
        // Stop at root or if parent smaller than child
        int root = Parent(position);
        if ( position <= 0 || m_Values[position] > m_Values[root] )
            return;
        
        // Otherwise swap and continue
        swap ( m_Values[position], m_Values[root] );
        BubbleUp ( root );
    }
    
public:
    
    CBinaryHeap () : m_Size (0), m_Max (1) {
        m_Values = new T[m_Max];
    }
    
    // O(n) construction
    CBinaryHeap ( T * start, size_t startSize ) {
        m_Max = m_Size = startSize;
        m_Values = new T[m_Max];
        for ( size_t i = 0; i < m_Size; ++i )
            m_Values[i] = start[i];
        BubbleDown();
    }
    
    ~CBinaryHeap () {
        delete [] m_Values;
    }
    
    CBinaryHeap ( const CBinaryHeap & other ) = delete;
    CBinaryHeap & operator = ( CBinaryHeap & other ) = delete;
    
    void Insert ( T element ) {
        if ( m_Size >= m_Max )
            Realloc();

        m_Values[m_Size] = element;
        BubbleUp(m_Size++);
    }
    
    bool ExtractMin ( T & result ) {
        if ( m_Size < 1 )
            return false;
        
        // Swap root and last element and remove it
        swap ( m_Values[m_Size - 1], m_Values[0] );
        result = m_Values[m_Size - 1];
        m_Size--;
        
        // Fix heap
        BubbleDown ();
        return true;
    }
    
    friend ostream & operator << ( ostream & os, const CBinaryHeap & heap ) {
        for ( size_t i = 0; i < heap.m_Size; i++ )
            os << heap.m_Values[i] << " ";
        return os;
    }
    
};

int main ( void ) {
    
    CBinaryHeap heap;
    
    cout << "Vitej v halde. Operace: + cislo, - [cokoliv] vypíše minimum, p [cokoliv] vypíše, q [cokoliv] konec" << endl;
    
    char c; int num;
    while ( cin >> c >> num ) {
        switch (c) {
            case 'q':
                cout << "Konec" << endl;
                return 0;
            case '+':
                cout << "Insert " << num << endl;
                heap.Insert (num);
                break;
            case '-':
                cout << "Extract ";
                if ( !heap.ExtractMin(num) )
                    cout << "failed" << endl;
                else
                    cout << num << endl;
                break;
            case 'p':
                cout << heap << endl;
                break;
            default:
                cout << "Neznámý příkaz." << endl;
                break;
        }
    }
    
}
