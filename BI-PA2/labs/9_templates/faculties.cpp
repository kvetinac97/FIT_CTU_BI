#include <iostream>
#include <set>
#include <stdexcept>
#include <string>
#include <typeinfo>

using namespace std;

template <class R, class T>

class CBodyBuilder {
    
    string m_Name;
    
    R m_Left;
    T m_Right;
    
public:

    CBodyBuilder ( string && name, R && left, T && right )
    : m_Name ( move(name) ), m_Left ( move(left) ), m_Right ( move(right) ) {}
    
    const string & getName () {
        return m_Name;
    }
    
    const R & getLeft () {
        return m_Left;
    }
    
    const T & getRight () {
        return m_Right;
    }
    
    friend bool operator < ( const CBodyBuilder<R, T> & l, const CBodyBuilder<R, T> & r ) {
        return l.m_Name < r.m_Name;
    }
    
};

template <class R, class T>

class CFaculty {
    
    set<CBodyBuilder<R, T>> m_Builders;
    
public:

    CFaculty () {
        // The worst thing
        if ( typeid(T) == typeid(string) )
            throw "Invalid type";
            
        // The best thing
        static_assert ( !is_same<T, float>::value, "Invalid" );
    }
    
    bool addBodyBuilder ( CBodyBuilder<R, T> builder ) {
        cout << "Added " << builder.getName() << " " << builder.getLeft() << endl;
        return m_Builders.insert ( builder ).second;
    }
    
};

// The bad thing for this - template specialization
// can be nicely used for some special "nice" cases
template <class R>

class CFaculty < R, double > {
    
public:
    CFaculty () {
        throw invalid_argument ( "Not valid!" );
    }
    
};

// Ideální třída na test šablon

class CTest {
    
    CTest () = delete;
    CTest & operator = ( const CTest & test ) = delete;
    
public:
    
    explicit CTest ( int i ) {}
    
    CTest ( const CTest & test ) {};
    CTest ( const CTest && test ) noexcept {};

};

// Abychom nemuseli pokaždé uvést <..., ...>
using CBodyBuilderT = CBodyBuilder<int, char>;

int main ( void ) {
    
    CFaculty <int, char> FIT;
    
    CBodyBuilderT David ( "\u001b[32mDavid\u001b[0m", 30, 'a' );
    CBodyBuilderT Roman ( "\u001b[34mRoman\u001b[0m", 32, 'b' );
    CBodyBuilderT Jarda ( "\u001b[31mJarda\u001b[0m", 40, 'c' );
    CBodyBuilderT  Adam ( "\u001b[36m Adam\u001b[0m", 35, 'd' );
    
    FIT.addBodyBuilder ( David );
    FIT.addBodyBuilder ( Roman );
    
    try {
        CFaculty <int, string> FEL;
        assert ( false );
    }
    catch ( const char * ) {}
    
    try {
        CFaculty <int, double> FEL;
        assert ( false );
    }
    catch ( const invalid_argument & ) {}
    
    // CFaculty <int, float> FEL;
    CFaculty <int, CTest> FEL;
    CBodyBuilder<int, CTest> Karel ( "Karel", 1, CTest(5) );
    FEL.addBodyBuilder ( Karel );
    
    return 0;
    
}

