#include <iostream>
#include <utility>
#include <cstddef>
#include <cassert>

class CLinkedList {
    
public:
	typedef double value_t;

private:

	struct CNode
	{
		value_t m_Value;
		CNode * m_Prev;
		CNode * m_Next;

		CNode ( const value_t & value, CNode * prev, CNode * next )
			: m_Value( value ), m_Prev( prev ), m_Next( next ) {}
	};

	CNode * m_Begin = nullptr;
	CNode * m_End = nullptr;
	size_t m_Size = 0;

	class CIterator
	{
	
		CNode * m_Node;
		friend class CLinkedList;

	public:
		CIterator ( CNode * node )
			: m_Node( node ) {}

		CIterator & operator ++ ()
		{
			m_Node = m_Node->m_Next;
			return *this;
		}

		value_t & operator * () const
		{ return m_Node->m_Value; }

		bool operator == ( const CIterator & other ) const
		{ return m_Node == other.m_Node; }
		
		bool operator != ( const CIterator & other ) const
		{ return !( *this == other ); }
		
	};

	class CConstIterator {

		CIterator m_Iterator;
		friend class CLinkedList;

	public:
		CConstIterator ( CNode * node )
			: m_Iterator( node ) {}
			
		CConstIterator ( const CIterator & iterator )
			: m_Iterator( iterator ) {}

		CConstIterator & operator ++ ()
		{
			++m_Iterator;
			return *this;
		}

		const value_t & operator * () const
		{ return *m_Iterator; }

		friend bool operator == ( const CConstIterator & lhs, const CConstIterator & rhs )
		{ return lhs.m_Iterator == rhs.m_Iterator; }
		
		friend bool operator != ( const CConstIterator & lhs, const CConstIterator & rhs )
		{ return !( lhs == rhs ); }
	};

public:
	typedef CIterator iterator;
	typedef CConstIterator const_iterator;

	~CLinkedList ()
	{
		for ( CNode * node = m_Begin; node; node = m_Begin ) {
			m_Begin = node->m_Next;
			delete node;
		}
	}
	
	void push_back ( const value_t & value )
	{
		CNode * node = new CNode { value, m_End, nullptr };
		( m_End ? m_End->m_Next : m_Begin ) = node;
		m_End = node;
		m_Size += 1;
	}

	void pop_back ()
	{
		CNode * node = m_End;
		( m_End->m_Prev ? m_End->m_Prev->m_Next : m_Begin ) = nullptr;
		m_End = m_End->m_Prev;
		m_Size -= 1;
		delete node;
	}

	const value_t & back () const
	{ return m_End->m_Value; }
	value_t & back ()
	{ return m_End->m_Value; }

	void push_front ( const value_t & value )
	{
		CNode * node = new CNode { value, nullptr, m_Begin };
		( m_Begin ? m_Begin->m_Prev : m_End ) = node;
		m_Begin = node;
		m_Size += 1;
	}

	void pop_front ()
	{
		CNode * node = m_Begin;
		( m_Begin->m_Next ? m_Begin->m_Next->m_Prev : m_End ) = nullptr;
		m_Begin = m_Begin->m_Next;
		m_Size -= 1;
		delete node;
	}

	const value_t & front () const
	{ return m_Begin->m_Value; }
	value_t & front ()
	{ return m_Begin->m_Value; }

	CIterator insert ( const const_iterator & it, const value_t & value )
	{
		CNode * next = it.m_Iterator.m_Node;
		CNode * node = new CNode { value, next ? next->m_Prev : m_End, next };

		if ( next ) {
			( next->m_Prev ? next->m_Prev->m_Next : m_Begin ) = node; 
			next->m_Prev = node;
		} else {
			( m_End ? m_End->m_Next : m_Begin ) = node;
			m_End = node;
		}

		m_Size += 1;
		return CIterator( node );
	}

	CIterator erase ( const const_iterator & it )
	{
		CNode * node = it.m_Iterator.m_Node;

		CNode * next = ( node->m_Prev ? node->m_Prev->m_Next : m_Begin ) = node->m_Next;
		( node->m_Next ? node->m_Next->m_Prev : m_End ) = node->m_Prev;

		m_Size -= 1;
		delete node;

		return CIterator( next );
	}

	bool empty () const
	{ return m_Size == 0; }

	size_t size () const
	{ return m_Size; }

	iterator begin ()
	{ return CIterator { m_Begin }; }
	const_iterator begin () const
	{ return CConstIterator { m_Begin }; }
	const_iterator cbegin () const
	{ return CConstIterator { m_Begin }; }

	iterator end ()
	{ return CIterator { nullptr }; }
	const_iterator end () const
	{ return CConstIterator { nullptr }; }
	const_iterator cend () const
	{ return CConstIterator { nullptr }; }
	
	// Set data from other list
	void copyFrom ( const CLinkedList & original ) {
	    const_iterator it = original.begin();
	    while ( it != original.end() ) {
	        push_back(*it);
	        ++it;
	    }
	}

	CLinkedList & operator = ( const CLinkedList & original ) {
	    // Clear old
	    while ( !empty() )
	        pop_back();

	    copyFrom ( original );
	    return *this;
	}
	
	CLinkedList () {}
	
	CLinkedList ( const CLinkedList & original ) {
	    copyFrom ( original );
	}
	
	void swap ( CLinkedList & other ) {
	    CLinkedList tmp ( other );
	    *this = other;
	    other = tmp;
	}
	
};

int main ()
{
	CLinkedList list1, list2;

	for ( size_t i = 0; i < 10; ++i ) {
		list1.push_back( 2.7 * i + 1.3 );
		list2.push_front( list1.back() );
	}
	
	list2 = list1;

	CLinkedList::iterator it2 = list2.begin();
	it2 = list2.insert( ++it2, 100.0 );
	assert( list1.size() != list2.size() );

	CLinkedList::iterator it1 = list1.begin();
	assert( list1.front() == list2.front() );
	assert( *(++it1) != *it2 );

	*it1 = 100.0;
	assert( *it1 == *it2 );
	assert( list1.front() == list2.front() );
	
	CLinkedList list3 = list1;
	list3.swap ( list1 );
	CLinkedList::iterator it3 = list3.begin();
	for ( it1 = list1.begin(); it1 != list1.end(); ++it1, ++it3 )
		assert( *it1 == *it3 );
	
	it3 = list3.begin();
	it3 = list3.erase( ++it3 );
	assert( list3.size() != list1.size() );
	
	it1 = list1.begin();
	++it1; ++it1;
	for ( ; it1 != list1.end(); ++it1, ++it3 )
		assert( *it1 == *it3 );

	return 0;
}
