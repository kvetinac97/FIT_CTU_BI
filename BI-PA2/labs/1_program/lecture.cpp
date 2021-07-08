// iostream nahrazuje stdio.h
#include <iostream>
#include <iomanip>

using namespace std;

// Jednodušší deklarace struktur!
struct LIST { int val; LIST * next; };

// Ukázkový příklad vstupněvýstupních parametrů
void umocniCislo ( int & cislo ) {
    cislo *= cislo; // tato funkce umocní číslo na druhou
}

// a přetěžování
void umocniCislo ( double & cislo ) {
    cislo *= cislo * cislo; // tato funkce umocní číslo na třetí
}

// nepovinný parametr
inline void nastavHodnotu ( int & cislo, int hodnota = 3 ) {
    cislo = hodnota;
}

// Zde už nepotřebujeme void
int main () {
    // Textová << konktatence, cout = stdout, endl = \n + flush
    cout << "Hello" << " " << "world!" << endl;
    
    // Konečně přichází typ bool !
    bool x = false;
    string y;
    
    cout << "Zadejte text:" << endl;
    cin >> y; // načtení ze vstupu, typ se sám nastaví, ws odstraní mezery
    cout << "X: " << boolalpha << x << " Y: " << y << endl; // manipulátor boolalpha
    
    // struktury i dynamická alokace
    LIST * head = new LIST;
    head->val = 5;
    cout << "Val: " << head->val << " Next: " << head->next << endl;
    delete head; // ekvivalent free

    // Pár výstupních manipulátorů
    cout << setw (10) << setfill ('+') << 5 << endl;
    cout << showbase << hex << 120 << endl;
    
    // Přetěžování a vstupněvýstupní parametry
    int a = 5, c;   double b = 8.0;
    umocniCislo(a);  umocniCislo(b);
    cout << noshowbase << dec << a << " " << b << endl;
    
    const int MAX = 100;
    // MAX = 5; chyba
    cout << "Max: " << MAX << endl;
    
    // Default parametry (od konce) a in-line funkce
    nastavHodnotu ( a );
    nastavHodnotu ( c, 4 );
    cout << a << " " << c << endl;
    
    // Alokace a zrušení pole
    int * pole = new int[1000];
    delete [] pole;
    
    // Klasické ukončení
    return 0;
}
