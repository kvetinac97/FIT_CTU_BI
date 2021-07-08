#include <iostream>

using namespace std;

class CArray {

    int * m_Arr;
    int  m_Size;

public:

    explicit CArray ( int size ) {
        m_Arr = new int[size];
        m_Size = size;
    }

    // Copying constructor
    CArray ( const CArray & source ) {
        m_Size = source.m_Size;
        m_Arr = new int[m_Size];
        for ( int i = 0; i < m_Size; i++ )
            m_Arr[i] = source[i];
    }

    int size () {
        return m_Size;
    }

    ~CArray () {
        delete [] m_Arr;
        m_Arr = nullptr;
        m_Size = 0;
    }

    int & operator [] ( int idx ) {
        return m_Arr[idx];
    }
    int operator [] ( int idx ) const {
        return m_Arr[idx];
    }

    CArray & operator = ( const CArray & source ) {
        // Optimization
        if ( &source == this )
            return *this;

        // Remove old data
        delete [] m_Arr;

        // Create new data
        m_Size = source.m_Size;
        m_Arr = new int[m_Size];
        for ( int i = 0; i < source.m_Size; i++ )
            m_Arr[i] = source[i];

        return *this;
    }

    friend ostream & operator << ( ostream & o, const CArray & x ) {
        for ( int i = 0; i < x.m_Size; i++ )
            o << x.m_Arr[i] << ' ';
        return o;
    }

};

int main () {

    CArray a ( 5 ), b ( 5 ), c ( a );
    for ( int i = 0; i < a.size ( ); i ++ )
        a[i] = i;

    cout << "a: " << a << endl; // [0 1 2 3 4]
    b = a; // shallow copy
    b[1] = 10;

    cout << "a: " << a << endl; // [0 10 2 3 4]
    cout << "b: " << b << endl; // [0 10/1 2 3 4]
    cout << "c: " << c << endl; // [0 1 2 3 4]

    return 0;
}
