#include <stdio.h>
#include <stdlib.h>

#define INIT_SIZE 4

/* Input-output argument "maze"
   Returns 0 if bad input occurs,
   returns EOF if EOF occurs,
   returns size of the row. */
int readMazeRow ( char ** maze, int * size, int * maxSize ) {
    char element;
    int rowSize = 0;

    while ( scanf ( &( ( *maze )[*size] ) ) != EOF ) { // *(char **) -> char *, *(int *) -> int
        element = ( *maze )[*size];

        rowSize++;
        (*size)++;

        if ( element == '\n' )
            break;

        if ( element != 'Z' && element != 'T' && element != 'H' ) {
            return 0;
        }

        /* Realloc */
        if ( *size == *maxSize ) {
            /* Enough memory assumption */
            *maxSize *= 2;
            *maze      = ( char * ) realloc ( *maze, *maxSize * sizeof ( char ) );
        }
    }

    return rowSize;
}

char * readMaze ( int * rowSize, int * rows ) {
    char * maze    = ( char * ) malloc ( INIT_SIZE * sizeof ( char ) );
    int    maxSize = INIT_SIZE,
           size    = 0,
           firstRowSize,
           secondRowSize;
    
    *rows = 0;

    /* Read first row */
    if ( ( firstRowSize = readMazeRow ( &maze, &size, &maxSize ) ) == 0 ) {
        free ( maze );
        return NULL;
    }

    *rows = 1;
    while ( ( secondRowSize = readMazeRow ( &maze, &size, &maxSize ) ) != EOF ) {
        if ( secondRowSize == 0 || firstRowSize != secondRowSize ) {
            free ( maze );
            return NULL;
        }
        
        (*rows)++;
    }
    
    *rowSize = firstRowSize;
    return maze;
}

int main( void ){
    char * maze;
    int    rowSize,
           rows;
    
    printf ( "Zadej bludiste:\n" );
    if ( ( maze = readMaze ( &rowSize, &rows ) ) == NULL){
        printf("Nespravny vstup.\n");
        return 1;
    }

    /* Prochazeni bludiste */

    free ( maze );
    return 0;
}
