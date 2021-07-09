#include <stdlib.h>
#include <stdio.h>

int howToPay ( int n, int m, int k, int * pole, int level ) {
    /* Ukončují/zárežející podmínky */
    if ( n == 0 && m == 0 ) {
        for ( int i = 0; i < level; ++i )
            printf ( "%d ", pole[ i ] );
        putchar ( '\n' );
        return 1;
    }

    int res = 0;
    if ( n > 0 ) {
        pole[ level ] = 100;
        res += howToPay ( n - 1, m    , k + 1, pole, level + 1 );
    }
    if ( m > 0 && k > 0 ) {
        pole[ level ] = 200;
        res += howToPay ( n    , m - 1, k - 1, pole, level + 1 );
    }
    return res;
}

int main ( void ) {
    int n = 8,
        m = 5;
    int * pole = ( int * ) malloc ( n * m * sizeof ( *pole ) );

    printf ( "Pocet moznosti: %d\n", howToPay ( n, m, 0, pole, 0 ) );
    free ( pole );
    return 0;
}
