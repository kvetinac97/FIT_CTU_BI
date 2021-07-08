#include <cstring>
#include <iostream>

using namespace std;

class CStr {

    char * m_Array; // contains '\0' at the end
    size_t   m_Len;
    size_t   m_Max;

public:

    CStr ( const char * str = "" ) {
        m_Len = strlen(str);
        m_Max = m_Len + 1; // '\0'

        m_Array = new char[ m_Max ];
        strncpy ( m_Array, str, m_Max );
    }

    CStr ( int x ) {
        m_Len = m_Max = 0;
        *this += x;
    }

    // Copy constructor
    CStr ( const CStr & source ) {
        m_Len = source.m_Len;
        m_Max = source.m_Max;

        m_Array = new char [ m_Max ];
        strncpy ( m_Array, source.m_Array, m_Max );
    }

    // Let's be paranoid
    ~CStr () {
        delete [] m_Array;
        m_Array = nullptr;
        m_Len = m_Max = 0;
    }

    // Append
    CStr & operator += ( const char * str ) {
        int length = m_Max + strlen ( str );
        char * arr = new char [ length ];

        strncpy ( arr, m_Array, m_Len );
        strncpy ( arr + m_Len, str, length - m_Len );

        delete [] m_Array;
        m_Array = arr;
        m_Len = length - 1;
        m_Max = length;
        return *this;
    }
    CStr & operator += ( const CStr & str ) {
        return ( *this += str.m_Array );
    }
    CStr & operator += ( int x ) {
        char buffer[20];
        snprintf ( buffer, sizeof(buffer), "%d", x );
        return ( *this += buffer );
    }

    // Copy operator
    CStr & operator = ( const CStr & source ) {
        if ( this == &source )
            return *this;

        delete [] m_Array;
        m_Len = source.m_Len;
        m_Max = source.m_Max;

        m_Array = new char [ m_Max ];
        strncpy ( m_Array, source.m_Array, m_Max );
        return *this;
    }

    // Length
    size_t Length () {
        return m_Len;
    }

    // Chars
    char & operator [] ( size_t idx ) {
        if ( idx < 0 || idx >= m_Len )
            throw "Index out of bounds!";

        return m_Array[idx];
    }

    // In-out
    friend istream & operator >> ( istream & is, CStr & str ) {
        char * string = new char[50];
        is >> string;
        str += string;
        delete [] string;
        return is;
    }
    friend ostream & operator << ( ostream & os, const CStr & str ) {
        return os << str.m_Array;
    }

    explicit operator const char * () const {
        return m_Array;
    }

};

int main () {

    CStr string;
    cin >> string;

    string += " (test)";

    CStr str2 = string;
    str2 += 3;
    cout << string << " " << str2 << endl;

    str2 = string;
    str2 = (str2 += str2);
    cout << str2 << endl;

    return 0;

}
