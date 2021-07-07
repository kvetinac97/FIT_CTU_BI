#include<stdio.h>
#include<math.h>

#define ARRAY_SIZE 1000000

// Load Erathosthen sieve
void loadSieve ( int composite[ARRAY_SIZE] ) {
    
    // Try every number
    for ( int i = 2; i < ARRAY_SIZE; i++ ) {
        
        if ( composite[i] ) // ignore
            continue;
        
        // Set composite numbers
        for ( int j = 2 * i; j < ARRAY_SIZE; j += i )
            composite[j] = 1;
        
    }
    
    /*// Výpis
    for ( int i = 2; i < ARRAY_SIZE; i++ )
        if ( !composite[i] )
            printf ( "%d je prvocislo\n", i );*/
        
}

// Factorise the number
void printFactorisation ( int composite[ARRAY_SIZE], long long int number ) {
        
    printf ( "Prvociselny rozklad cisla %lld je:\n", number );
    
    if ( number == 1 ) { // special case
        printf ( "1\n" );
        return;
    }
    
    // Repeat loop till number is 1 / prime
    while ( number > ARRAY_SIZE || composite[number] ) {
        
        // Try every number
        for ( int i = 2; i <= sqrt(number) + 1; i++ ) {
            
            // Is prime divisor
            if ( number % i == 0 ) {
                
                int power = 0;
                
                // Find highest power in number
                for ( ; number % i == 0; number /= i )
                    power ++;
                
                // Print
                if ( power == 1 )
                    printf ( "%d", i );
                else
                    printf ( "%d^%d", i, power );

                printf ( number == 1 ? "\n" : " x " );
                                
            }
            
        }
        
    }
    
    // Is prime, we need to print
    if ( number != 1 )
        printf ( "%lld\n", number );
    
}

int main ( void ) {
    
    // Composite number array
    int composite[ARRAY_SIZE] = {0};
    loadSieve ( composite );

    long long int number = -1;
    
    // Loading
    while ( scanf ( "%lld", &number ) == 1 && number > 0 ) {
        printFactorisation ( composite, number );
        number = -1;
    }
    
    // Error
    if ( number < 0 ) {
        fprintf ( stderr, "Error: Chybny vstup!\n" );
        return 100;
    }
    
    return 0;
    
}
