#include <iostream>
#include <fstream>

using namespace std;

const int NUMBER_COUNT = 16;

int main () {
    
    int numbers[NUMBER_COUNT];
    cout << "Zadejte cisla:" << endl;
    
    for ( int i = 0; i < NUMBER_COUNT; i++ )
        if ( ! (cin >> numbers[i]) ) {
            cout << "Nespravny vstup." << endl;
            return 1;
        }
    
    ofstream outputFile ( "test.bin", ios::binary );
    
    if ( !outputFile ) {
        cout << "Chyba v osouboru." << endl;
        return 1;
    }
    
    for ( int i = 0; i < NUMBER_COUNT; i++ ) {
        
        if ( !outputFile.write( (const char *) &numbers[i], sizeof(numbers[i]) ) ) {
            cout << "Chyba pri zapisu do souboru." << endl;
            return 1;
        }
        
    }
    
    outputFile.close();
    
    ifstream inputFile ( "test.bin", ios::binary );
    
    if ( !inputFile ) {
        cout << "Chyba v isouboru." << endl;
        return 1;
    }
    
    int i = 0;
    while ( ( inputFile.read( reinterpret_cast<char*>(&numbers[i]), sizeof(numbers[i]) ) ) ) {
        cout << numbers[i] << " ";
        i++;
    }
    cout << endl;
    
    if ( !inputFile.eof() ) {
        cout << "Chyba pri cteni." << endl;
        return 1;
    }
    
    return 0;
    
}
