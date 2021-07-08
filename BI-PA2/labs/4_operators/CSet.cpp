#include "CSet.hpp"

bool CSet::contains ( const value_t & v ) const
{
    for ( size_t i = 0; i < m_Data.size(); i++ )
        if ( m_Data[i] == v )
            return true;
    
    return false;
}

CSet & CSet::add ( const value_t & v )
{
    m_Data.push_back ( v );
    return *this;
}

CSet & CSet::remove ( const value_t & v )
{
    for ( size_t i = 0; i < m_Data.size(); i++ )
        if ( m_Data[i] == v )
            m_Data.erase( m_Data.begin() + i );
    
    return *this;
}

bool CSet::isSubsetOf ( const CSet & other ) const
{
    for ( size_t i = 0; i < m_Data.size(); i++ )
        if ( !other.contains(m_Data[i]) )
            return false;
    
    return true;
}
