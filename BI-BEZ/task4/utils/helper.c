#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/**
 * Helper testing program generating random data of given size
 * source: https://stackoverflow.com/a/16549706
 */
int main ( int argc, char ** argv ) {
    long long i, s;
    FILE * f = fopen ( argv[1], "w" );
    srand ( time(NULL) );
    sscanf(argv[2], "%lld", &s);
    for ( i = 0; i < s; i++ )
        fputc ( rand() % 255, f );
    fclose(f);
    return 0;
}
