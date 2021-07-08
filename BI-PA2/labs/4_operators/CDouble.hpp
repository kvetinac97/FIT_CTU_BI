#pragma once

#include <cmath>
#include <iostream>

using namespace std;

class CDouble {
    
    double m_Prev;
    double m_Next;
    
public:
    
    CDouble ( double x )
    : m_Prev ( x - 1 ), m_Next ( x + 1 ) {}
    
    CDouble ( double from, double to )
    : m_Prev ( from ), m_Next ( to ) {}
        
    friend bool operator < ( const CDouble & l, const CDouble & r ) {
        return l.m_Prev < r.m_Prev && l.m_Next < r.m_Prev;
    }
    
    friend bool operator > ( const CDouble & l, const CDouble & r ) {
        return l.m_Prev > r.m_Prev && l.m_Next > r.m_Prev;
    }
    
    friend ostream & operator << ( ostream & os, const CDouble & dbl ) {
        return os << dbl.get();
    }
    
    friend istream & operator >> ( istream & is, CDouble & dbl ) {
        return is >> dbl.m_Prev >> dbl.m_Next;
    }
    
    CDouble operator + ( const CDouble & other ) {
        return CDouble ( m_Prev + other.m_Prev, m_Next + other.m_Next );
    }
    
    CDouble & operator += ( const CDouble & other ) {
        return *this = *this + other;
    }
    
    CDouble operator - ( const CDouble & other ) {
        return CDouble ( m_Prev - other.m_Prev, m_Next - other.m_Next );
    }
    
    CDouble & operator -= ( const CDouble & other ) {
        return *this = *this - other;
    }
    
    CDouble operator - () {
        return CDouble ( 0 - m_Prev, 0 - m_Next );
    }
    
    CDouble operator / ( const CDouble & other ) {
        if ( other.get() == 0 )
            throw "Division by zero!";
        
        return CDouble ( m_Prev / other.m_Prev, m_Next / other.m_Next );
    }
    
    bool operator == ( const CDouble & other ) {
        return ( ( m_Prev >= other.m_Prev && m_Prev <= other.m_Next ) || ( m_Next >= other.m_Prev && m_Next <= other.m_Next ) );
    }
    
    double get () const;
    
};
