#pragma once

#include <iostream>

class CMod
{
private:
	int m_Mod;
	int m_Number;

public:
	explicit CMod ( int mod, int number = 0 );

	CMod & operator++ ();
	CMod operator++ ( int );

	CMod & operator-- ();
	CMod operator-- ( int );

	CMod operator+ ( const CMod & other ) const;
	CMod operator- () const;
	CMod operator- ( const CMod & other ) const;
	CMod operator* ( const CMod & other ) const;

	CMod & operator+= ( const CMod & other );
	CMod & operator-= ( const CMod & other );
	CMod & operator*= ( const CMod & other );

	bool operator== ( const CMod & other ) const;
	bool operator== ( int other ) const;

	bool operator!= ( const CMod & other ) const;
	bool operator!= ( int other ) const;

	static CMod createRandom ( int mod = 0 );

	friend std::ostream & operator<< ( std::ostream & out, const CMod & self );
	friend std::istream & operator>>( std::istream & in, CMod & self );
};

bool operator== ( int lhs, const CMod & rhs );
bool operator!= ( int lhs, const CMod & rhs );

std::ostream & operator<< ( std::ostream & out, const CMod & self );
std::istream & operator>>( std::istream & in, CMod & self );
