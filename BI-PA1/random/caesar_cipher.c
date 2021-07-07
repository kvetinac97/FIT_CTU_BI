#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int loadText ( char ** text, int * length ) {
    
    // Allocate initial size
    int i = 0, size = 0, maxSize = 10;
    char c, * txt = (char *) malloc ( maxSize * sizeof(*txt) );
    
    while ( ( c = getchar() ) != '\n' && c != EOF ) {
        // Invalid input
        if ( c < 'A' || c > 'z' || ( c > 'Z' && c < 'a' ) )
            return 0;
        
        // Need to realloc
        if ( size >= maxSize ) {
            maxSize += ( maxSize / 2 ); // 3/2x greater
            txt = (char *) realloc ( txt, maxSize * sizeof(*txt) );
        }
        
        txt[i++] = c;
    }
    
    // Write data
    *length = i;
    *text = txt;
    return 1;
    
}

void shift ( char * text, int length ) {
    
    // Realize the shift
    for ( int i = 0; i < length; i++ ) {
                
        // "Overflow" : Z -> a, z -> A
        if ( text[i] == 'Z' )
            text[i] = 'a';
        else if ( text[i] == 'z' )
            text[i] = 'A';
        else
            text[i] = text[i] + 1; // shift one step higher
        
    }
    
}

int compare ( char * text1, char * text2, int length ) {
    
    int matches = 0;
    
    // Load every character, increase match count if same
    for ( int i = 0; i < length; i++ )
        if ( text1[i] == text2[i] )
            matches++;
    
    return matches;
    
}

int main ( void ) {
    
    char * text1 = NULL, * text2 = NULL;
    int length1 = 0, length2 = 0;
    
    // Invalid text
    if ( !loadText ( &text1, &length1 ) || !loadText ( &text2, &length2 ) ) {
        
        fprintf ( stderr, "Error: Chybny vstup!\n" );
        free ( text1 );
        free ( text2 );
        return 100;
        
    }
    
    if ( length1 != length2 ) {
        
        fprintf ( stderr, "Error: Chybna delka vstupu!\n" );
        free ( text1 );
        free ( text2 );
        return 101;
        
    }
    
    char * bestMatch = (char *) malloc ( length1 * sizeof(*bestMatch) );
    int bestScore = -1;
    
    // Try every possible combination
    for ( int i = 0; i < 52; i++ ) {
        
        shift ( text1, length1 );
        int score = compare ( text1, text2, length1 );
        
        // Better, copy result
        if ( score > bestScore ) {
            bestScore = score;
            strncpy ( bestMatch, text1, length1 );
        }
        
    }
    
    // Print result
    for ( int i = 0; i < length1; i++ ) {
        printf ( "%c", bestMatch[i] );
    }
    printf ( "\n" );
    
    // Free memory, end
    free ( bestMatch );
    free ( text1 );
    free ( text2 );
    return 0;
    
}

