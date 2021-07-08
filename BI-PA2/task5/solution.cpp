#include <iostream>
#include <iomanip>
#include <set>
#include <list>
#include <map>
#include <string>
#include <algorithm>

using namespace std;

class CTimeStamp {
    int m_Year;
    int m_Month;
    int m_Day;
    int m_Hour;
    int m_Minute;
    int m_Sec;
public:
    CTimeStamp ( int year, int month, int day, int hour, int minute, int sec )
    : m_Year ( year ), m_Month ( month ), m_Day ( day ),
      m_Hour ( hour ), m_Minute ( minute ), m_Sec ( sec ) {}

    int Compare ( const CTimeStamp & x ) const {
        auto l = tie ( m_Year, m_Month, m_Day, m_Hour, m_Minute, m_Sec );
        auto r = tie ( x.m_Year, x.m_Month, x.m_Day, x.m_Hour, x.m_Minute, x.m_Sec );
        return ( l < r ? -1 : ( ( l > r ) ? 1 : 0 ) );
    }
    friend ostream & operator << ( ostream & os, const CTimeStamp & x ) {
        return os << x.m_Year << "-" << setfill('0') << setw(2) << x.m_Month << "-" << setfill('0') << setw(2) << x.m_Day << " "
        << setfill('0') << setw(2) << x.m_Hour << ":" << setfill('0') << setw(2) << x.m_Minute << ":" << setfill('0') << setw(2) << x.m_Sec;
    }
};
//=================================================================================================
class CMailBody {
    int    m_Size;
    string m_Data;
public:
    CMailBody ( int size, const char * data )
    : m_Size ( size ), m_Data ( string ( data ) ) {}
    friend ostream & operator << ( ostream & os, const CMailBody & x ) {
        return os << "mail body: " << x . m_Size << " B";
    }
};
//=================================================================================================
class CAttach {
    int            m_X;
    mutable int    m_RefCnt;
    CAttach ( const CAttach & x );
    CAttach & operator = ( const CAttach   & x );
    ~CAttach ( void ) = default;
    friend ostream & operator << ( ostream & os, const CAttach & x ) {
        return os << "attachment: " << x . m_X << " B";
    }
public:
    CAttach ( int x ) : m_X (x), m_RefCnt ( 1 ) {}
    void AddRef ( void ) const { m_RefCnt ++; }
    void Release ( void ) const {
        if ( !--m_RefCnt )
            delete this;
    }
};
//=================================================================================================
class CMail {
    CTimeStamp   m_Timestamp;
    string            m_From;
    CMailBody         m_Body;
    const CAttach * m_Attach;

    void copyFrom ( const CMail & src ) {
        m_Attach = src.m_Attach;
        if ( m_Attach )
            m_Attach->AddRef();
    }
public:
    CMail ( const CTimeStamp & timeStamp, const string & from,
            const CMailBody  & body, const CAttach * attach )
       : m_Timestamp ( timeStamp ), m_From ( from ),
         m_Body ( body ), m_Attach ( attach ) { if ( m_Attach ) m_Attach->AddRef(); }
    CMail ( const CMail & src )
    : m_Timestamp ( src.m_Timestamp ), m_From ( src.m_From ), m_Body ( src.m_Body ) {
        copyFrom ( src );
    }
    ~CMail () { if ( m_Attach ) m_Attach->Release(); }
    CMail & operator = ( const CMail & src ) {
        m_Attach->Release();
        m_Timestamp = src.m_Timestamp;
        m_From = src.m_From;
        m_Body = src.m_Body;

        copyFrom ( src );
        return *this;
    }

    const string & From ( void ) const {
        return m_From;
    }
    const CMailBody & Body ( void ) const { return m_Body; }
    const CTimeStamp & TimeStamp ( void ) const { return m_Timestamp; }
    const CAttach * Attachment ( void ) const { return m_Attach; }

    friend ostream & operator << ( ostream & os, const CMail & x ) {
        os << x.m_Timestamp << " " << x.m_From << " " << x.m_Body;
        if ( x.m_Attach )
            os << " + " << *(x.m_Attach);
        return os;
    }
};
//=================================================================================================
struct CMailComp {
    bool operator() ( const CMail & l, const CMail & r ) const {
        return l.TimeStamp().Compare(r.TimeStamp()) < 0;
    }
};
//=================================================================================================
class CMailBox {
    map<string, set<CMail, CMailComp>> m_Folders;
public:
    CMailBox ( void ) {
         NewFolder ("inbox");
     }

    bool Delivery ( const CMail & mail ) {
        return m_Folders["inbox"] . insert ( mail ).second;
    }
    bool NewFolder ( const string & folderName ) {
        return m_Folders.insert ( {folderName, set<CMail, CMailComp>()} ).second;
    }
    bool MoveMail ( const string & fromFolder, const string & toFolder ) {
        if ( m_Folders.find( fromFolder ) == m_Folders.end() ||
            m_Folders.find( toFolder ) == m_Folders.end() )
            return false;

        m_Folders[toFolder].merge ( m_Folders[fromFolder] );
        m_Folders[fromFolder].clear();
        return true;
    }

    list<CMail> ListMail ( const string & folderName, const CTimeStamp & from, const CTimeStamp & to ) const {
        list<CMail> mails;
        auto start = m_Folders.at(folderName).lower_bound ( CMail (from, "", CMailBody(0, ""), nullptr) );
        auto end = m_Folders.at(folderName).upper_bound ( CMail (to, "", CMailBody(0, ""), nullptr) );

        for ( auto it = start; it != end; it++ )
            mails.push_back ( *it );

        return mails;
    }
    set<string> ListAddr ( const CTimeStamp & from, const CTimeStamp & to ) const {
         set<string> addresses;
         for ( auto it = m_Folders.begin(); it != m_Folders.end(); it++ ) {
             auto start = it->second.lower_bound ( CMail (from, "", CMailBody(0, ""), nullptr) );
             auto end = it->second.upper_bound ( CMail (to, "", CMailBody(0, ""), nullptr) );

             for ( auto it2 = start; it2 != end; it2++ )
                 addresses.insert ( it2->From() );
         }
         return addresses;
    }
};
