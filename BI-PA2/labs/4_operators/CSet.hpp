#pragma once

#include <vector>
#include "CMod.hpp"

class CSet
{
public:
	typedef CMod value_t;

private:
	std::vector<value_t> m_Data;

	bool contains ( const value_t & v ) const;

	CSet & add ( const value_t & v );

	CSet & remove ( const value_t & v );

	bool isSubsetOf ( const CSet & other ) const;

public:
	size_t size () const
	{ return m_Data.size(); }

	bool operator() ( const value_t & v ) const
	{ return contains( v ); }

	const value_t & operator[] ( size_t idx ) const
	{ return m_Data[ idx ]; }
	value_t & operator[] ( size_t idx )
	{ return m_Data[ idx ]; }

	CSet & operator+= ( const value_t & v )
	{ return add( v ); }
	CSet operator+ ( const value_t & v ) const
	{ return CSet{ *this } += v; }

	CSet & operator-= ( const value_t & v )
	{ return remove( v ); }
	CSet operator- ( const value_t & v ) const
	{ return CSet{ *this } -= v; }

	bool operator<< ( const CSet & other ) const
	{ return isSubsetOf( other ); }
	
	bool operator>> ( const CSet & other ) const
	{ return other.isSubsetOf( *this ); }
};
