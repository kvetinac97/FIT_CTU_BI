#include <iostream>
#include <sstream>
#include <string>

using namespace std;

/*
 * U prvku T předpokládáme
 *  - implicitní konstruktor
 *  - destuktor
 *  - operátor =
 *
 *  - operátor ==
 *  - operátor <<
 *  - operátor <
 */

class CT {

    int m_Val;
    CT ( const CT & ) = delete;

public:

    CT ( int val = 0 ) : m_Val ( val ) {}
    // implicitní destruktor a operátor =

    friend bool operator < ( const CT & l, const CT & r ) {
        return l.m_Val < r.m_Val;
    }

    friend bool operator == ( const CT & l, const CT & r ) {
        return !(l < r) && !(r < l);
    }

    friend ostream & operator << ( ostream & os, const CT & t ) {
        return os << t.m_Val;
    }

};

template <class T>
class CSet {

    size_t m_Size = 0;
    size_t m_Max = 0;
    T * m_Data = nullptr;

    // Data manipulation
    void reallocate ( size_t max ) {
        T * tmp = new T [ m_Max = max ];

        for ( size_t i = 0; i < m_Size; i++ )
            tmp[i] = m_Data[i];

        m_Data = tmp;
    }
    void del () {
        delete [] m_Data;
        m_Size = m_Max = 0;
        m_Data = nullptr;
    }
    void copy ( const CSet<T> & src ) {
        m_Size = src.m_Size;
        m_Data = new T [ m_Max = src.m_Max ];

        for ( size_t i = 0; i < m_Size; i++ )
            m_Data[i] = src.m_Data[i];
    }

    // Searching
    T * lower_bound ( const T & el ) {

        size_t low = 0, high = m_Size;

        while ( low != high ) {
            size_t n = ( low + high ) / 2;
            if ( m_Data[n] == el )
                return m_Data + n;

            if ( m_Data[n] < el )
                low = n + 1;
            else
                high = n;
        }

        return m_Data + low;

    }

public:

    CSet () { reallocate(8); }
    CSet ( const CSet<T> & src ) { copy ( src ); }
    ~CSet () { del(); }
    CSet & operator = ( const CSet<T> & src ) {
        if ( &src == this )
            return *this;

        del();
        copy ( src );
        return *this;
    }

    size_t card () {
        return m_Size;
    }

    void erase ( const T & el ) {
        T * pos = lower_bound ( el );
        if ( pos == m_Data + m_Size )
            throw "Element not found!";

        for ( size_t i = pos - m_Data; (i + 1) < m_Size; i++ )
            m_Data[i] = m_Data[i + 1];

        m_Size --;
    }

    CSet & operator += ( const T & el ) {
        T * pos = lower_bound ( el );
        if ( pos != m_Data + m_Size && *pos == el ) // already contains
            return *this;

        if ( m_Size == m_Max )
            reallocate ( m_Max + (m_Max / 2) );

        if ( m_Size >= 1 )
            for ( size_t i = m_Size - 1; m_Data + i >= pos; i-- ) {
                m_Data[i + 1] = m_Data[i];
                if ( i == 0 )
                    break;
            }

        *pos = el;
        m_Size ++;
        return *this;
    }
    CSet & operator += ( const CSet<T> & set ) {
        for ( size_t i = 0; i < set.m_Size; i++ )
            *this += set.m_Data[i];
        return *this;
    }

    friend ostream & operator << ( ostream & os, const CSet & set ) {
        os << "{";
        for ( size_t i = 0; (i + 1) < set.m_Size; i++ )
            os << set.m_Data[i] << ", ";

        if ( set.m_Size >= 1 )
            os << set.m_Data[set.m_Size - 1];
        return os << "}";
    }

};


// Test
int main () {

    ostringstream os;

    CSet<CT> set;
    os << set;

    assert ( set.card() == 0 );
    assert ( os.str() == "{}" );

    ( ( ( set += 5 ) += 3 ) += 2 ) += 3;
    os.str("");
    os << set;

    assert ( set.card() == 3 );
    assert ( os.str() == "{2, 3, 5}" );

    CSet<CT> set2;
    ( set2 += 1 ) += 2;
    os.str("");
    os << set2;

    assert ( set2.card() == 2 );
    assert ( os.str() == "{1, 2}" );

    CSet<CT> set3 ( set );
    set3 += set2;
    os.str("");
    os << set3;

    assert ( set3.card() == 4 );
    assert ( os.str() == "{1, 2, 3, 5}" );

    set3 += 4;
    os.str("");
    os << set3;

    assert ( set3.card() == 5 );
    assert ( os.str() == "{1, 2, 3, 4, 5}" );

    set.erase ( 5 );
    set.erase ( 3 );
    os.str("");
    os << set;

    assert ( set3.card() == 5 );
    assert ( set.card() == 1 );
    assert ( os.str() == "{2}" );

    try {
        set.erase ( 4 );
        assert ( false );
    }
    catch ( const char * ) {}

    set3.erase ( 5 );
    os.str("");
    os << set3;

    assert ( set3.card() == 4 );
    assert ( os.str() == "{1, 2, 3, 4}" );

    set2.erase ( 1 );
    os.str("");
    os << set2;

    assert ( set2.card() == 1 );
    assert ( os.str() == "{2}" );

    set2 = set3;
    assert ( set2.card() == 4 );

    set2.erase ( 4 );
    assert ( set3.card() == 4 );
    assert ( set2.card() == 3 );

    CSet<string> str;
    os.str("");
    os << str;

    assert ( str.card() == 0 );
    assert ( os.str() == "{}" );

    ( ( ( str += "Adolf" ) += "Cecilka" ) += "Barbora" ) += "Cecilka";
    os.str("");
    os << str;

    assert ( str.card() == 3 );
    assert ( os.str() == "{Adolf, Barbora, Cecilka}" );

    CSet<string> str2;
    ( str2 += "Carel" ) += "Barbora";
    os.str("");
    os << str2;

    assert ( str2.card() == 2 );
    assert ( os.str() == "{Barbora, Carel}" );

    CSet<string> str3 ( str );
    str3 += str2;
    os.str("");
    os << str3;

    assert ( str3.card() == 4 );
    assert ( os.str() == "{Adolf, Barbora, Carel, Cecilka}" );

    str3 += "Emanuel";
    os.str("");
    os << str3;

    assert ( str3.card() == 5 );
    assert ( os.str() == "{Adolf, Barbora, Carel, Cecilka, Emanuel}" );

    str.erase ( "Barbora" );
    str.erase ( "Cecilka" );
    os.str("");
    os << str;

    assert ( str3.card() == 5 );
    assert ( str.card() == 1 );
    assert ( os.str() == "{Adolf}" );

    try {
        str.erase ( "Emanuel" );
        assert ( false );
    }
    catch ( const char * ) {}

    str3.erase ( "Emanuel" );
    os.str("");
    os << str3;

    assert ( str3.card() == 4 );
    assert ( os.str() == "{Adolf, Barbora, Carel, Cecilka}" );

    str2.erase ( "Barbora" );
    os.str("");
    os << str2;

    assert ( str2.card() == 1 );
    assert ( os.str() == "{Carel}" );

    str2 = str3;
    assert ( str2.card() == 4 );

    str2.erase ( "Barbora" );
    assert ( str3.card() == 4 );
    assert ( str2.card() == 3 );

    return 0;

}