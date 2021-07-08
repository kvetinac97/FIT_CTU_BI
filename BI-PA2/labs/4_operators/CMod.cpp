#include "CMod.hpp"

#include <string>
#include <cstdlib>

using namespace std;

CMod::CMod ( int mod, int number )
	: m_Mod( mod ), m_Number( number )
{
	if ( mod < 2 )
		throw "Modulus must be greater/equal than 2.";
	number = number % mod;
	if ( number < 0 )
		number += mod;
}

CMod & CMod::operator++ ()
{
	m_Number += 1;
	if ( m_Number >= m_Mod )
		m_Number %= m_Mod;
	return *this;
}

CMod CMod::operator++ ( int )
{
	CMod tmp = *this;
	++*this;
	return tmp;
}

CMod & CMod::operator-- ()
{
	if ( m_Number == 0 )
		m_Number = m_Mod;
	m_Number -= 1;
	return *this;
}

CMod CMod::operator-- ( int )
{
	CMod tmp = *this;
	--*this;
	return tmp;
}

CMod CMod::operator+ ( const CMod & other ) const
{
	if ( m_Mod != other.m_Mod )
		throw "Modulus of both operands must be same.";
	return CMod { m_Mod, ( m_Number + other.m_Number ) % m_Mod };
}

CMod CMod::operator- () const
{
	return CMod { m_Mod, ( m_Mod - m_Number ) % m_Mod };
}

CMod CMod::operator- ( const CMod & other ) const
{
	return *this + -other;
}

CMod CMod::operator* ( const CMod & other ) const
{
	if ( m_Mod != other.m_Mod )
		throw "Modulus of both operands must be same.";
	return CMod { m_Mod, ( m_Number * other.m_Number ) % m_Mod };
}

CMod & CMod::operator+= ( const CMod & other )
{
	return *this = *this + other;
}

CMod & CMod::operator-= ( const CMod & other )
{
	return *this = *this - other;
}

CMod & CMod::operator*= ( const CMod & other )
{
	return *this = *this * other;
}

bool CMod::operator== ( const CMod & other ) const
{
	if ( m_Mod != other.m_Mod )
		return false;
	return m_Number == other.m_Number;
}

bool CMod::operator== ( int other ) const
{
	return m_Number == ( ( other % m_Mod ) + m_Mod ) % m_Mod;
}

bool CMod::operator!= ( const CMod & other ) const
{
	return !( *this == other );
}

bool CMod::operator!= ( int other ) const
{
	return !( *this == other );
}

CMod CMod::createRandom ( int mod )
{
	if ( mod < 2 )
		mod = rand() % 10 + 2;
	return CMod { mod, rand() % mod };
}

bool operator== ( int lhs, const CMod & rhs )
{
	return rhs == lhs;
}

bool operator!= ( int lhs, const CMod & rhs )
{
	return rhs != lhs;
}

ostream & operator<< ( ostream & out, const CMod & self )
{
	return out << self.m_Number << " (mod " << self.m_Mod << ")";
}

istream & operator>>( istream & in, CMod & self )
{
	int number, mod;
	char par1, par2;
	string str;

	if ( !( in >> number >> par1 >> str >> mod >> par2 ) )
		return in;
	
	if ( par1 != '(' || str != "mod" || par2 != ')' ) {
		in.clear( ios::failbit );
		return in;
	}

	try {
		self = CMod { mod, number };
	} catch ( const char * ) {
		in.clear( ios::failbit );
	}
	
	return in;
}
