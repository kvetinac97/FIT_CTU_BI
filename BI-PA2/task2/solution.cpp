#include <algorithm>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

/*
 * Class Account
 * used for representing one person's account
 */
class CAccount {

    // Private properties
    string m_Account;
    int m_Income;
    int m_Expense;

public:

    // Constructor
    CAccount ( string account )
    : m_Account ( account ), m_Income ( 0 ), m_Expense ( 0 ) {}

    // Getter
    string GetAccount () const {
        return m_Account;
    }

    // "Setters"
    void AddIncome ( int income ) {
        m_Income += income;
    }
    void AddExpense ( int expense ) {
        m_Expense += expense;
    }

    void Export ( string & account, int & income, int & expense ) const {
        account = m_Account;
        income = m_Income;
        expense = m_Expense;
    }

    // Overloaded function for easier sort (lower_bound)
    friend bool operator < ( const CAccount & left, const CAccount & right ) {
        return left.m_Account < right.m_Account;
    }

};

/*
 * Class CPerson
 * used for representing one person
 */
class CPerson {

    // Private properties
    string m_Name;
    string m_Addr;
    string m_Account;

public:

    // Constructor
    CPerson ( string name, string addr, string account = "" )
    : m_Name ( name ), m_Addr ( addr ), m_Account ( account ) {}

    // Getters
    string GetName () const {
        return m_Name;
    }
    string GetAddr () const {
        return m_Addr;
    }
    string GetAccount () const {
        return m_Account;
    }

    // Overloaded comparing function for easier sort (lower_bound)
    friend bool operator < ( const CPerson & left, const CPerson & right ) {
        return left.m_Name == right.m_Name ? left.m_Addr < right.m_Addr
            : left.m_Name < right.m_Name;
    }

};

/*
 * Class CIterator
 * Used for iterating over CPerson vector
 */
class CIterator {

    // Private properties - iterators
    vector<CPerson>::const_iterator m_Pos;
    vector<CPerson>::const_iterator m_End;

public:

    // Iterating over vector
    bool AtEnd ( void ) const {
        return m_Pos == m_End;
    }
    void Next ( void ) {
        ++ m_Pos;
    }

    // Getters
    string Name ( void ) const {
        return m_Pos->GetName();
    }
    string Addr ( void ) const {
        return m_Pos->GetAddr();
    }
    string Account ( void ) const {
        return m_Pos->GetAccount();
    }

    // Constructor
    CIterator ( const vector<CPerson> & people )
    : m_Pos ( people.begin() ), m_End ( people.end() ) {}

};

/*
 * Class CTaxRegister
 * Main class containing all methods
 */
class CTaxRegister {

    // Classic iterators for changing values
    typedef vector<CPerson>::iterator  iterator_p;
    typedef vector<CAccount>::iterator iterator_a;

    // Constant iterators for getting values
    typedef vector<CPerson>::const_iterator  constant_p;
    typedef vector<CAccount>::const_iterator constant_a;

    // Private properties
    vector<CPerson> people;
    vector<CAccount> accounts;

    // Methods to check duplicates, with const variants
    bool Contains ( const string & name, const string & addr, constant_p & it ) const {
        it = lower_bound ( people.begin(), people.end(), CPerson { name, addr } );
        return ( it != people.end() && it->GetName() == name && it->GetAddr() == addr );
    }
    bool Contains ( const string & name, const string & addr, iterator_p & it ) {
        return Contains ( name, addr, (constant_p &) it );
    }

    bool Contains ( const string & account, constant_a & it ) const {
        it = lower_bound ( accounts.begin(), accounts.end(), CAccount { account } );
        return ( it != accounts.end() && it->GetAccount() == account );
    }
    bool Contains ( const string & account, iterator_a & it ) {
        return Contains ( account, (constant_a &) it );
    }

public:

    // Adds a new person
    bool Birth ( const string & name, const string & addr, const string & account ) {

        iterator_a it_account;
        iterator_p it_person;

        // Check duplicates
        if ( Contains( name, addr, it_person ) || Contains ( account, it_account ) )
            return false;

        // Add
        accounts.insert ( it_account, CAccount ( account ) );
        people.insert ( it_person, CPerson { name, addr, account } );
        return true;

    }

    // Removes a person
    bool Death ( const string & name, const string & addr ) {

        iterator_a it_account;
        iterator_p it_person;

        // We must find the person
        if ( !Contains( name, addr, it_person ) || !Contains ( it_person->GetAccount(), it_account ) )
            return false;

        // Remove
        accounts.erase ( it_account );
        people.erase ( it_person );
        return true;

    }

    // Increases account's/person's income
    bool Income ( const string & account, int amount ) {

        iterator_a it;

        // Account must exist
        if ( !Contains ( account, it ) )
            return false;

        it->AddIncome ( amount );
        return true;

    }
    bool Income ( const string & name, const string & addr, int amount ) {

        iterator_p it;

        // Person must exist
        if ( !Contains ( name, addr, it ) )
            return false;

        // Use already defined function
        return this->Income ( it->GetAccount(), amount );

    }

    // Increases account's/person's expense
    bool Expense ( const string & account, int amount ) {

        iterator_a it;

        // Account must exist
        if ( !Contains ( account, it ) )
            return false;

        it->AddExpense ( amount );
        return true;

    }
    bool Expense ( const string & name, const string & addr, int amount ) {

        iterator_p it;

        // Person must exist
        if ( !Contains ( name, addr, it ) )
            return false;

        // Use already defined function
        return this->Expense ( it->GetAccount(), amount );

    }

    // Exports person's account, income and expense
    bool Audit ( const string & name, const string & addr,
            string & account, int & sumIncome, int & sumExpense ) const {

        // Constant - we're in constant function, not changing anything
        constant_a it_account;
        constant_p it_person;

        // Person and account must exist
        if ( !Contains ( name, addr, it_person ) || !Contains ( it_person->GetAccount(), it_account ) )
            return false;

        it_account->Export ( account, sumIncome, sumExpense );
        return true;
    }

    // Get iterator for iterating over people, sorted by 1. names 2. address
    CIterator ListByName ( void ) const {
        return CIterator ( people );
    }

};

int main ( void )
{
    string acct;
    int    sumIncome, sumExpense;
    CTaxRegister b1;
    assert ( b1 . Birth ( "John Smith", "Oak Road 23", "123/456/789" ) );
    assert ( b1 . Birth ( "Jane Hacker", "Main Street 17", "Xuj5#94" ) );
    assert ( b1 . Birth ( "Peter Hacker", "Main Street 17", "634oddT" ) );
    assert ( b1 . Birth ( "John Smith", "Main Street 17", "Z343rwZ" ) );
    assert ( b1 . Income ( "Xuj5#94", 1000 ) );
    assert ( b1 . Income ( "634oddT", 2000 ) );
    assert ( b1 . Income ( "123/456/789", 3000 ) );
    assert ( b1 . Income ( "634oddT", 4000 ) );
    assert ( b1 . Income ( "Peter Hacker", "Main Street 17", 2000 ) );
    assert ( b1 . Expense ( "Jane Hacker", "Main Street 17", 2000 ) );
    assert ( b1 . Expense ( "John Smith", "Main Street 17", 500 ) );
    assert ( b1 . Expense ( "Jane Hacker", "Main Street 17", 1000 ) );
    assert ( b1 . Expense ( "Xuj5#94", 1300 ) );
    assert ( b1 . Audit ( "John Smith", "Oak Road 23", acct, sumIncome, sumExpense ) );
    assert ( acct == "123/456/789" );
    assert ( sumIncome == 3000 );
    assert ( sumExpense == 0 );
    assert ( b1 . Audit ( "Jane Hacker", "Main Street 17", acct, sumIncome, sumExpense ) );
    assert ( acct == "Xuj5#94" );
    assert ( sumIncome == 1000 );
    assert ( sumExpense == 4300 );
    assert ( b1 . Audit ( "Peter Hacker", "Main Street 17", acct, sumIncome, sumExpense ) );
    assert ( acct == "634oddT" );
    assert ( sumIncome == 8000 );
    assert ( sumExpense == 0 );
    assert ( b1 . Audit ( "John Smith", "Main Street 17", acct, sumIncome, sumExpense ) );
    assert ( acct == "Z343rwZ" );
    assert ( sumIncome == 0 );
    assert ( sumExpense == 500 );
    CIterator it = b1 . ListByName ();
    assert ( ! it . AtEnd ()
             && it . Name () == "Jane Hacker"
             && it . Addr () == "Main Street 17"
             && it . Account () == "Xuj5#94" );
    it . Next ();
    assert ( ! it . AtEnd ()
             && it . Name () == "John Smith"
             && it . Addr () == "Main Street 17"
             && it . Account () == "Z343rwZ" );
    it . Next ();
    assert ( ! it . AtEnd ()
             && it . Name () == "John Smith"
             && it . Addr () == "Oak Road 23"
             && it . Account () == "123/456/789" );
    it . Next ();
    assert ( ! it . AtEnd ()
             && it . Name () == "Peter Hacker"
             && it . Addr () == "Main Street 17"
             && it . Account () == "634oddT" );
    it . Next ();
    assert ( it . AtEnd () );

    assert ( b1 . Death ( "John Smith", "Main Street 17" ) );

    CTaxRegister b2;
    assert ( b2 . Birth ( "John Smith", "Oak Road 23", "123/456/789" ) );
    assert ( b2 . Birth ( "Jane Hacker", "Main Street 17", "Xuj5#94" ) );
    assert ( !b2 . Income ( "634oddT", 4000 ) );
    assert ( !b2 . Expense ( "John Smith", "Main Street 18", 500 ) );
    assert ( !b2 . Audit ( "John Nowak", "Second Street 23", acct, sumIncome, sumExpense ) );
    assert ( !b2 . Death ( "Peter Nowak", "5-th Avenue" ) );
    assert ( !b2 . Birth ( "Jane Hacker", "Main Street 17", "4et689A" ) );
    assert ( !b2 . Birth ( "Joe Hacker", "Elm Street 23", "Xuj5#94" ) );
    assert ( b2 . Death ( "Jane Hacker", "Main Street 17" ) );
    assert ( b2 . Birth ( "Joe Hacker", "Elm Street 23", "Xuj5#94" ) );
    assert ( b2 . Audit ( "Joe Hacker", "Elm Street 23", acct, sumIncome, sumExpense ) );
    assert ( acct == "Xuj5#94" );
    assert ( sumIncome == 0 );
    assert ( sumExpense == 0 );
    assert ( !b2 . Birth ( "Joe Hacker", "Elm Street 23", "AAj5#94" ) );

    return 0;
}