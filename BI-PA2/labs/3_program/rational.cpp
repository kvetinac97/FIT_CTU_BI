#include <iostream>

using namespace std;

class CRat {

    int m_Num;
    int m_Den;

public:

    CRat ( int num = 0, int den = 1 )
    : m_Num ( num ), m_Den ( den ) {}

    // CRat + CRat
    CRat operator + ( const CRat & r ) const {
        return CRat ( ( m_Num * r.m_Den ) + ( r.m_Num * m_Den ), m_Den * r.m_Den );
    }

    // CRat + num, 3/4 + 2
    CRat operator + ( int num ) const {
        return *this + CRat ( num );
    }

    CRat operator - () const {
        return CRat ( -m_Num, m_Den );
    }

    // num + CRat
    friend CRat operator + ( int num, const CRat & r ) {
        return r + num;
    }

    friend ostream & operator << ( ostream & os, const CRat & r ) {
        return os << r.m_Num << " / " << r.m_Den << endl;
    }

    friend istream & operator >> ( istream & is, CRat & r ) {
        int num, den;
        if ( ! ( is >> num >> den ) )
            return is;

        r.m_Num = num;
        r.m_Den = den;
        return is;
    }

    operator double () {
        return ( (double) m_Num ) / m_Den;
    }

};

int main () {

    cout << "Type num and den, please:" << endl;
    CRat num = CRat ( 5, 3 ), num2 ( 0, 0 );
    if ( ! ( cin >> num2 ) )
        throw "Invalid input.";

    CRat num3 = num + -num2,
         num4 = num + 3,
         num5 = 4 + num2;

    cout << num << num2 << num3 << num4 << ( (double) num5 ) << endl;

    return 0;

}
