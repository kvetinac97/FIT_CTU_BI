#include <cstring>
#include <iostream>
#include <sstream>

using namespace std;

class CHistory {
    char * m_History;
    int m_Uses = 0;

public:
    CHistory ( const char * history = "" ) {
        m_History = new char[strlen(history) + 1];
        strcpy ( m_History, history );
    }

    ~CHistory() {
        delete [] m_History;
    }

    CHistory ( const CHistory & src ) {
        m_History = new char[strlen(src.m_History) + 1];
        strcpy ( m_History, src.m_History );
    }

    CHistory & operator = ( const CHistory & src ) {
        delete [] m_History;
        m_History = new char[strlen(src.m_History) + 1];
        strcpy ( m_History, src.m_History );
        return *this;
    }

    friend ostream & operator << ( ostream & os, const CHistory & history ) {
        return os << history.m_History;
    }

    friend class CBankAccount;

};

class CBankAccount {

    char * m_Name;
    CHistory ** m_Histories;
    int m_Cnt = 0;

    int m_Start;
    int m_Balance;

    void Destruct () {
        delete [] m_Name;

        for ( int i = 0; i < m_Cnt; i++ )
            if ( --m_Histories[i]->m_Uses == 0 )
                delete m_Histories[i];

        delete [] m_Histories;
    }

    void Create ( const char * name, int start, int balance ) {
        m_Name = new char [strlen(name) + 1];
        strcpy ( m_Name, name );

        m_Start = start;
        m_Balance = balance;
    }

    void AttachHistory ( const CBankAccount & src ) {
        m_Cnt = src.m_Cnt;
        m_Histories = new CHistory * [src.m_Cnt + 32];
        for ( int i = 0; i < src.m_Cnt; i++ ) {
            m_Histories[i] = src.m_Histories[i];
            m_Histories[i]->m_Uses++;
        }
    }

public:

    CBankAccount ( const char * name = "", int balance = 0 ) {
        Create ( name, balance, balance );

        m_Histories = new CHistory * [32];
        m_Histories[0] = new CHistory ( "" );
        m_Histories[0]->m_Uses = 1;
        m_Cnt = 1;
    }

    CBankAccount ( const CBankAccount & src ) {
        Create ( src.m_Name, src.m_Start, src.m_Balance );
        AttachHistory ( src );
    }
    ~CBankAccount () {
        Destruct();
    }
    CBankAccount & operator = ( const CBankAccount & src ) {
        Destruct();
        Create ( src.m_Name, src.m_Start, src.m_Balance );
        AttachHistory ( src );
        return *this;
    }

    int Balance () {
        return m_Balance;
    }

    void AddTransaction ( int amount, const char * to, const char * sign ) {
        ostringstream os;

        if ( amount < 0 )
            os << " - " << -amount << ", to: " << to << ", sign: " << sign << endl;
        else
            os << " + " << amount << ", from: " << to << ", sign: " << sign << endl;

        m_Balance += amount;

        if ( m_Histories[m_Cnt - 1]->m_Uses > 1 ) {
            CHistory * tmp = new CHistory ( os.str().c_str() );
            tmp->m_Uses = 1;
            m_Histories[m_Cnt++] = tmp;
            return;
        }

        int len = strlen(os.str().c_str()) + 1;
        char * tmpResult = new char [ len ];
        strcpy ( tmpResult, os.str().c_str() );

        char * tmp = new char [ strlen(m_Histories[m_Cnt - 1]->m_History) + len ];
        strcpy ( tmp, m_Histories[m_Cnt - 1]->m_History );
        strncpy ( tmp + strlen(m_Histories[m_Cnt - 1]->m_History), tmpResult, len );

        delete [] m_Histories[m_Cnt - 1]->m_History;
        m_Histories[m_Cnt - 1]->m_History = tmp;
        delete [] tmpResult;
    }

    void Trim () {
        for ( int i = 0; i < m_Cnt; i++ )
            if ( --m_Histories[i]->m_Uses == 0 )
                delete m_Histories[i];

        delete [] m_Histories;

        m_Histories = new CHistory * [32];
        m_Histories[0] = new CHistory ( "" );
        m_Histories[0]->m_Uses = 1;
        m_Cnt = 1;
        m_Start = m_Balance;
    }

    friend ostream & operator << ( ostream & os, const CBankAccount & acc ) {
        os << acc.m_Name << ":\n   " << acc.m_Start << "\n";
        for ( int i = 0; i < acc.m_Cnt; i++ )
            os << *(acc.m_Histories[i]);
        os << " = " << acc.m_Balance << "\n";
        return os;
    }

    friend bool operator < ( const CBankAccount & l, const CBankAccount & r ) {
        int l_len = strlen(l.m_Name), r_len = strlen(r.m_Name);
        return strncmp ( l.m_Name, r.m_Name, l_len < r_len ? l_len : r_len ) < 0;
    }
    friend bool operator == ( const CBankAccount & l, const CBankAccount & r ) {
        return !(l < r) && !(r < l);
    }
    friend bool operator != ( const CBankAccount & l, const CBankAccount & r ) {
        return !(l == r);
    }

    friend class CBankData;

};

class CBankData {

    CBankAccount * m_Accounts;
    int m_Size = 0, m_Max = 0;

public:

    int m_Uses = 0;

    CBankData () : m_Size ( 0 ), m_Max ( 0 ) {
        m_Accounts = new CBankAccount [ m_Max ];
    }
    ~CBankData() {
        delete [] m_Accounts;
        m_Accounts = nullptr; // paranoid
    }

    CBankData ( const CBankData & src ) {
        m_Size = src.m_Size;
        m_Max = src.m_Max;

        m_Accounts = new CBankAccount [ m_Max ];
        for ( int i = 0; i < m_Size; i++ )
            m_Accounts[i] = src.m_Accounts[i];
    }

    CBankData & operator = ( const CBankData & src ) {
        delete [] m_Accounts;
        m_Size = src.m_Size;
        m_Max = src.m_Max;

        m_Accounts = new CBankAccount [ m_Max ];
        for ( int i = 0; i < m_Size; i++ )
            m_Accounts[i] = src.m_Accounts[i];
        return *this;
    }

    bool contains ( const char * name, int & pos ) {
        for ( int i = 0; i < m_Size; i++ ) {
            if ( strncmp ( m_Accounts[i].m_Name, name, strlen(name) ) == 0 ) {
                pos = i;
                return true;
            }
        }

        return false;
    }

    void NewAccount ( const char * accID, int initialBalance ) {
        // reallocate
        if ( m_Max < m_Size + 2 ) {
            m_Max += ( m_Max < 32 ? 8 : m_Max / 2 );
            CBankAccount * tmp = new CBankAccount [ m_Max ];
            for ( int i = 0; i < m_Size; i++ )
                tmp[i] = m_Accounts[i];
            delete [] m_Accounts;
            m_Accounts = tmp;
        }

        m_Accounts[m_Size++] = CBankAccount ( accID, initialBalance );
    }
    int NewAccountPos ( const char * accID ) {
        NewAccount(accID, 0);
        return m_Size - 1;
    }

    void AddTransaction ( int pos, unsigned int amount, const char * other, const char * signature ) {
        m_Accounts[pos].AddTransaction(amount, other, signature);
    }

    void TrimAccount ( int pos ) {
        m_Accounts[pos].Trim();
    }

    CBankAccount Account ( int pos ) {
        return m_Accounts[pos];
    }

    CBankAccount & AccountRef ( int pos ) {
        return m_Accounts[pos];
    }

};

class CBank {

    CBankData ** m_Data;
    int m_Cnt;

    void attach ( const CBank & src ) {
        m_Cnt = src.m_Cnt + 1;
        m_Data = new CBankData * [m_Cnt + 16];

        for ( int i = 0; i < src.m_Cnt; i++ ) {
            m_Data[i] = src.m_Data[i];
            m_Data[i]->m_Uses++;
        }

        m_Data[m_Cnt - 1] = new CBankData;
        m_Data[m_Cnt - 1]->m_Uses = 1;
    }

    bool contains ( const char * acc, int & dataPos, int & accPos ) {
        // Our own first, other second (prevent duplicates)
        for ( int i = m_Cnt - 1; i >= 0; i-- )
            if ( m_Data[i]->contains(acc, accPos) ) {
                dataPos = i;
                return true;
            }

        return false;
    }
    void destroy () {
        for ( int i = 0; i < m_Cnt; i++ )
            if ( --m_Data[i]->m_Uses == 0 )
                delete m_Data[i];

        delete [] m_Data;
    }

public:

    // Data
    CBank () {
        m_Cnt = 1;
        m_Data = new CBankData * [32];
        m_Data[0] = new CBankData;
        m_Data[0]->m_Uses = 1;
    }
    ~CBank () {
        destroy ();
    }

    CBank ( const CBank & bank ) {
        attach ( bank );
    }
    CBank & operator = ( const CBank & bank ) {
        destroy ();
        attach ( bank );
        return *this;
    }

    bool NewAccount ( const char * accID, int initialBalance ) {
        int dataPos, accPos;
        if ( contains(accID, dataPos, accPos) )
            return false;

        m_Data[m_Cnt - 1]->NewAccount( accID, initialBalance );
        return true;
    }

    bool Transaction ( const char * debAccID, const char * credAccID,
                       unsigned int amount, const char * signature ) {
        if ( strncmp ( debAccID, credAccID, strlen(debAccID) ) == 0 )
            return false;

        int debDataPos, debAccPos, credDataPos, credAccPos;
        if ( !contains(debAccID, debDataPos, debAccPos) ||
             !contains(credAccID, credDataPos, credAccPos) )
            return false;

        if ( m_Data[debDataPos]->m_Uses > 1 ) {

            if ( m_Cnt - 1 == debDataPos ) {
                CBankData * tmp = new CBankData;
                tmp->m_Uses = 1;
                m_Data[m_Cnt++] = tmp;
            }

            int newPos = m_Data[m_Cnt - 1]->NewAccountPos(debAccID);
            m_Data[m_Cnt - 1]->AccountRef(newPos) = m_Data[debDataPos]->Account(debAccPos);

            debDataPos = m_Cnt - 1;
            debAccPos = newPos;
        }

        if ( m_Data[credDataPos]->m_Uses > 1 ) {

            if ( m_Cnt - 1 == credDataPos ) {
                CBankData * tmp = new CBankData;
                tmp->m_Uses = 1;
                m_Data[m_Cnt++] = tmp;
            }

            int newPos = m_Data[m_Cnt - 1]->NewAccountPos(credAccID);
            m_Data[m_Cnt - 1]->AccountRef(newPos) = m_Data[credDataPos]->Account(credAccPos);

            credDataPos = m_Cnt - 1;
            credAccPos = newPos;
        }

        m_Data[debDataPos]->AddTransaction(debAccPos, -amount, credAccID, signature);
        m_Data[credDataPos]->AddTransaction(credAccPos, amount, debAccID, signature);
        return true;
    }

    bool TrimAccount ( const char * accID ) {
        int dataPos, accPos;
        if ( !contains(accID, dataPos, accPos) )
            return false;

        if ( m_Data[dataPos]->m_Uses > 1 ) {

            if ( m_Cnt - 1 == dataPos ) {
                CBankData * tmp = new CBankData;
                tmp->m_Uses = 1;
                m_Data[m_Cnt++] = tmp;
            }

            int newPos = m_Data[m_Cnt - 1]->NewAccountPos(accID);
            m_Data[m_Cnt - 1]->AccountRef(newPos) = m_Data[dataPos]->Account(accPos);

            dataPos = m_Cnt - 1;
            accPos = newPos;
        }

        m_Data[dataPos]->TrimAccount( accPos );
        return true;
    }

    CBankAccount Account ( const char * accID ) {
        int dataPos, accPos;
        if ( !contains(accID, dataPos, accPos) )
            throw "Account not found";

        return m_Data[dataPos]->Account( accPos );
    }

};