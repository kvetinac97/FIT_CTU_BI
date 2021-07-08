#include <algorithm>
#include <cassert>
#include <iostream>
#include <string>

using namespace std;

class CCar {
    string m_SPZ;
    int m_Age;
    string m_Owner;
    
public:
    
    /*CCar ( string SPZ = "", int age = 0, string owner = "" ) {
        m_SPZ = SPZ;
        m_Age = age;
        m_Owner = owner;
    }*/
    
    // Lepší zápis:
     CCar ( string SPZ = "", int age = 0, string owner = "" )
      : m_SPZ ( SPZ ), m_Age ( age ), m_Owner ( owner ) {}
    
    friend bool operator == ( const CCar & lcar, const CCar & rcar ) {
        return lcar.m_SPZ == rcar.m_SPZ && lcar.m_Age == rcar.m_Age && lcar.m_Owner == rcar.m_Owner;
    }
    
    friend bool operator < ( const CCar & lcar, const CCar & rcar ) {
        return lcar.m_Age < rcar.m_Age;
    }
    
    friend ostream & operator << ( ostream & os, const CCar & car ) {
        return os << car.m_SPZ << " " << car.m_Age << " " << car.m_Owner;
    }
    
};

struct CCarField {
    
    CCarField () {
        m_Cars = new CCar[m_Max];
    }
    
    ~CCarField () {
        delete [] m_Cars;
    }
    
    CCar /*& */operator [] ( int pos ) {
        if ( pos < 0 || pos > m_Count )
            throw "Out of range";
        
        return m_Cars[pos];
    }
    
    void add ( const CCar & car ) {
        if ( m_Count - 1 == m_Max )
            realloc ();
        
        m_Cars[m_Count++] = car;
    }
    
    void sort ( ) {
        std::sort ( m_Cars, m_Cars + m_Count );
    }
    
    friend ostream & operator << ( ostream & os, const CCarField & field ) {
        for ( int i = 0; i < field.m_Count; i++ )
            cout << field.m_Cars[i] << endl;
        return os;
    }
    
    bool remove ( const CCar & car ) {
        
        int removed = 0;
        for ( int i = 0; i < m_Count; i++ )
            if ( m_Cars[i] == car )
                removed++;
            else
                m_Cars[i - removed] = m_Cars[i];
        
        m_Count -= removed;
        return removed > 0;
        
    }
        
private:
    
    CCar * m_Cars = nullptr;
    
    int m_Count = 0;
    int m_Max = 8;
    
    void realloc ( ) {
        m_Max += ( m_Max / 2 );
        
        CCar * tmp = new CCar[m_Max];
        copy ( tmp, tmp + m_Max, m_Cars );
        
        delete [] m_Cars;
        m_Cars = tmp;
    }
    
};


int main ( void ) {
    
    CCarField field;
    cout << field;
    
    field.add( CCar { "3T4-0554", 10, "Karel Novák" } );
    field.add( CCar { "10F-PGTS", 25, "Josef Novák" } );
    field.add( CCar { "10F-PGTS", 25, "Josef Novák" } );
    
    cout << field << endl;
    cout << field[0] << endl;
    
    field.add( CCar { "PRO-GTES", 17, "Joško Novák" } );
    cout << field << endl;
    
    field.sort ();
    cout << field << endl;
    
    assert ( field.remove( CCar { "PRO-GTES", 17, "Joško Novák" } ) );
    cout << field << endl;
    
    assert ( !field.remove( CCar { "HOO-TEST", 17, "Joško Novák" } ) );
    cout << field << endl;
    
    assert ( field.remove( CCar { "10F-PGTS", 25, "Josef Novák" } ) );
    cout << field << endl;
    assert ( field.remove( CCar { "3T4-0554", 10, "Karel Novák" } ) );
    
    return 0;
    
}
