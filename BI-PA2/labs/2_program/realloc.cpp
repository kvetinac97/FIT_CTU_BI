#include <iostream>

using namespace std;

void realloc ( int *& array, int size, int newSize ) {
    int * nova = new int[newSize];
    copy ( array, array + size, nova );
    delete [] array;
    array = nova;
}

int main () {
    
    int * whyarewestillhere = new int[32546560];
    
    for ( int i = 0; i < 32546560; i++ )
        whyarewestillhere[i] = ( i % 42 ) * i;
    
    cout << "Why are we still here?" << endl;
    
    realloc ( whyarewestillhere, 32546560, 325465600 );
    whyarewestillhere[32546564] = 353;
    
    cout << "Just to suffer... " << whyarewestillhere[454] << " " << whyarewestillhere[32546564] << endl;
    
    return 0;
    
}
