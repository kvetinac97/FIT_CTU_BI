#include "stdio.h"
#include "stdlib.h"
#include "string.h"

int splitWord ( char * word, int * spaces, int length, int pos );

int main ( void ) {
    // Load world
    char * word = NULL; size_t wordSize = 0;
    getline(&word, &wordSize, stdin);

    int length = strlen(word);
    word[--length] = '\0';
    
    if ( length <= 0 || length > 100 ) {
        printf ("Nespravny vstup.\n");
        free (word);
        return 1;
    }
    
    int positions [100];
    for ( int i = 0; i < 100; i++ )
        positions[i] = 0;
        
    int cnt = splitWord ( word, positions, length, 0 );
    printf ("Celkem: %d\n", cnt);
        
    free ( word );
    return 0;
}

int splitWord ( char * word, int * spaces, int length, int pos ) {
    // Print word
    if ( pos == length ) {
        // Verify spaces
        for ( int i = 0; i < length - 1; i++ )
            if (spaces[i] && spaces[i + 1])
                return 0;

        for ( int i = 0; i < length; i++ ) {
            if (spaces[i])
                printf (" ");
            printf ("%c", word[i]);
        }
        printf("\n");
        return 1;
    }
    
    // Put space
    spaces[pos] = 1;
    int res1 = pos == 0 ? 0 : splitWord ( word, spaces, length, pos + 1 );
    spaces[pos] = 0; // clean up
    
    // Do not put space
    int res2 = splitWord ( word, spaces, length, pos + 1 );
    return res1 + res2;
}
