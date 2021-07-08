/*
 * Dneska se konečně naučíme dělat OOP v C++
 */
#include <iostream>
#include <fstream>

using namespace std;

// Toto je třída
class VIP {
    // Tady jsou privátní proměnné
    double testo;
    // a funkce
    void SetDouble ( double nova ) {
        testo = nova;
    }
    // A tady veřejné
    public:
    // proměnné
    int property;
    // a funkce, včetně konstruktoru
    VIP ( int propDef ) {
        this->property = propDef;
        this->testo = 0;
    }
    void SetDoubleExternal ( double nova ) {
        SetDouble ( nova );
    }
    double GetDouble () {
        return testo;
    }
    // a destruktoru
    ~VIP () {
        cout << "Destrukce VIP !" << endl;
    }

};

int main () {
    // Takto se s třídami pracuje
    VIP * test = new VIP (5);
    
    cout << test->GetDouble() << endl;
    test->SetDoubleExternal(3.2);
    cout << test->GetDouble() << " " << test->property << endl;
    
    // Nesmíme zapomenout uvolnit
    delete test;
    // konec
    return 0;
}
