#include <iostream>
#include <cassert>
#include <cstring>
#include <cctype>
#include <string>
#include <sstream>
#include <algorithm>
#include <stdexcept>

using namespace std;

class CBigInt {

    // Represented number
    string m_Num = "";

    CBigInt abs () const {
        CBigInt tmp;
        tmp.m_Num = ( m_Num[0] == '-' ) ? m_Num.substr ( 1, m_Num.length() - 1 ) : m_Num;
        return tmp;
    }

public:

    CBigInt ( int num = 0 ) {

        stringstream stream;
        stream << num;
        stream >> m_Num;

    }

    CBigInt ( const char * original ) {

        int len = strlen ( original );
        const char * num = original;

        // Negative numbers
        if ( len > 0 && num[0] == '-' ) {
            num ++;
            len --;
            m_Num.push_back ( '-' );
        }

        // Invalid numbers: "" or "-"
        if ( len == 0 )
            throw invalid_argument ( original );

        bool notZero = false;

        for ( int i = 0; i < len; i++ ) {

            char c = num[i];
            if ( c < '0' || c > '9' )
                throw invalid_argument ( original );

            if ( c == '0' && !notZero )
                continue;

            notZero = true;
            m_Num.push_back ( c );

        }

    }

    // I-O
    friend ostream & operator << ( ostream & os, const CBigInt & num ) {
        return os << num.m_Num;
    }
    friend istream & operator >> ( istream & is, CBigInt & num ) {

        string str = "";
        bool beginZero = false;
        char c;

        while ( ( c = is.peek() ) != EOF ) {

            if ( str.length() == 0 ) {

                if ( isspace ( c ) ) { // skip ws
                    is.get();
                    continue;
                }

                if ( ( c < '0' || c > '9' ) && c != '-' ) { // invalid input
                    is.setstate ( ios::failbit );
                    return is;
                }

                is.get(); // really load
                if ( c == '0' )
                    beginZero = true;
                str.push_back ( c );
                continue;

            }

            if ( beginZero ) {

                if ( c == '0' ) { // invalid input "00..."
                    is.setstate ( ios::failbit );
                    return is;
                }
                else
                    beginZero = false;

            }

            if ( str.length() == 1 && str[0] == '-' && c == '0' )
                beginZero = true;

            if ( c < '0' || c > '9' ) {

                if ( str.length() == 1 && str[0] == '-' ) { // invalid input "-"
                    is.setstate ( ios::failbit );
                    return is;
                }

                break; // stop loading

            }

            is.get(); // really load
            str.push_back ( c );

        }

        num.m_Num = str;
        return is;

    }

    // Comparing
    friend bool operator < ( const CBigInt & l, const CBigInt & r ) {

        bool l_neg = l.m_Num[0] == '-', r_neg = r.m_Num[0] == '-';

        if ( l_neg && !r_neg )
            return true;
        if ( r_neg && !l_neg )
            return false;

        if ( l.m_Num.length() < r.m_Num.length() )
            return true;

        if ( r.m_Num.length() < l.m_Num.length() )
            return false;

        for ( auto li = l.m_Num.begin(), ri = r.m_Num.begin(); li < l.m_Num.end(); li++, ri++ ) {
            if ( *li < *ri )
                return true;
            if ( *ri < *li )
                return false;
        }

        // equal
        return false;

    }
    friend bool operator == ( const CBigInt & l, const CBigInt & r ) {
        return !(l < r) && !(r < l);
    }
    friend bool operator != ( const CBigInt & l, const CBigInt & r ) {
        return !(l == r);
    }
    friend bool operator <= ( const CBigInt & l, const CBigInt & r ) {
        return (l < r) || (l == r);
    }
    friend bool operator > ( const CBigInt & l, const CBigInt & r ) {
        return !(l <= r);
    }
    friend bool operator >= ( const CBigInt & l, const CBigInt & r ) {
        return !(l < r);
    }

    // Sum
    friend CBigInt operator + ( const CBigInt & l, const CBigInt & r ) {

        CBigInt sum;
        string tmp = "";

        bool l_neg = ( l.m_Num[0] == '-' ), r_neg = ( r.m_Num[0] == '-' );

        // Adding
        if ( ( l_neg && r_neg ) || ( !l_neg && !r_neg ) ) {

            int trans = 0;

            for ( auto li = l.m_Num.rbegin(), ri = r.m_Num.rbegin();
                  li < l.m_Num.rend() - l_neg || ri < r.m_Num.rend() - r_neg;
                  li++, ri++ ) {

                int left = ( li < l.m_Num.rend() - l_neg ? ( *li - '0' ) : 0 );
                int right = ( ri < r.m_Num.rend() - r_neg ? ( *ri - '0' ) : 0 );

                int summary = ( left + right + trans );
                if ( summary >= 10 ) {
                    trans = ( summary / 10 );
                    summary = ( summary % 10 );
                }
                else
                    trans = 0;

                tmp.push_back ( '0' + summary );

            }

            if ( trans > 0 )
                tmp.push_back ( '0' + trans );

            while ( tmp.back() == '0' && tmp.length() > 1 )
                tmp.pop_back();

            if ( l_neg )
                tmp.push_back ( '-' );

        }
            // Subtracting
        else {

            CBigInt l_abs = l.abs(), r_abs = r.abs();
            bool swap = false;

            // shortcut
            if ( l_abs == r_abs )
                return CBigInt ();

            if ( l_abs < r_abs ) {
                CBigInt tmp = l_abs;
                l_abs = r_abs;
                r_abs = tmp;
                swap = true;
            }

            int trans = 0;

            for ( auto li = l_abs.m_Num.rbegin(), ri = r_abs.m_Num.rbegin();
                  li < l_abs.m_Num.rend() || ri < r_abs.m_Num.rend();
                  li++, ri++ ) {

                int left = ( li < l_abs.m_Num.rend() ? ( *li - '0' ) : 0 );
                int right = ( ri < r_abs.m_Num.rend() ? ( *ri - '0' ) : 0 );

                int summary = ( left - right - trans );

                if ( summary < 0 ) {
                    trans = 1;
                    summary = 10 + summary;
                }
                else
                    trans = 0;

                tmp.push_back ( '0' + summary );

            }

            while ( tmp.back() == '0' && tmp.length() > 1 )
                tmp.pop_back();

            if ( ( !swap && l_neg ) || ( swap && r_neg ) )
                tmp.push_back ( '-' );

        }

        sum.m_Num = string ( tmp.rbegin(), tmp.rend() );
        return sum;

    }
    CBigInt & operator += ( const CBigInt & other ) {
        CBigInt tmp = ( *this ) + other;
        m_Num = tmp.m_Num;
        return *this;
    }

    // Times
    friend CBigInt operator * ( const CBigInt & l, const CBigInt & r ) {
        // Easy
        if ( l == 0 || r == 0 )
            return CBigInt ();

        CBigInt tmp, tmp2, ln, rn;
        string str = "";

        bool l_neg = ( l.m_Num[0] == '-' ), r_neg = ( r.m_Num[0] == '-' ),
                result_neg = ( l_neg && !r_neg ) || ( !l_neg && r_neg );

        // Switch, so bigger number is at left side
        if ( l.abs() < r.abs() ) {
            ln = r.abs(); rn = l.abs();
        }
        else {
            ln = l.abs(); rn = r.abs();
        }

        for ( auto r_it = rn.m_Num.rbegin(); r_it < rn.m_Num.rend(); r_it ++ ) {

            str.clear();

            for ( auto i = 0; i < r_it - rn.m_Num.rbegin(); i++ )
                str.push_back ( '0' );

            int trans = 0;
            for ( auto l_it = ln.m_Num.rbegin(); l_it < ln.m_Num.rend(); l_it ++ ) {

                int left = ( *l_it - '0' ), right = ( *r_it - '0' ),
                        summary = ( trans + ( left * right ) );

                if ( summary >= 10 ) {
                    trans = ( summary / 10 );
                    summary = ( summary % 10 );
                }
                else
                    trans = 0;

                str.push_back ( '0' + summary );

            }

            if ( trans > 0 )
                str.push_back ( '0' + trans );

            if ( result_neg )
                str.push_back ( '-' );

            tmp2.m_Num = string ( str.rbegin(), str.rend() );
            tmp += tmp2;
        }

        return tmp;
    }
    CBigInt & operator *= ( const CBigInt & other ) {
        CBigInt tmp = ( *this ) * other;
        m_Num = tmp.m_Num;
        return *this;
    }

};