#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define SUCCESS 1
#define FAILURE 0

/*
 * Program počítající průměr mezd
 * na vstupu jsou zadány regiony ve formátu { průměr A, průměr B }
 * region je zastoupen právě jedním písmenem A-Z
 *
 * na druhém řádku bude napsán dotaz na průměry mezd v regionech
 * AB, AAB, ABB, AABB, AFGGJHWQWGFFSDFTB = spočítá průměrnou mzdu z A a B
 * XXX = napíše N/A, jelikož průměrná mzda nepasuje s žádným regionem
 */
typedef struct {
    int num;
    int sum;
} TREGION;

int loadRegions ( TREGION regions[] ) {
    
    int num;
    char c;
    
    if ( scanf ( " %c", &c ) != 1 || c != '{' )
        return FAILURE;
        
    while ( scanf ( "%d %c", &num, &c ) == 2 ) {
                
        if ( c < 'A' || c > 'Z' || num < 0 )
            return FAILURE;
        
        regions[c - 'A'].num++;
        regions[c - 'A'].sum += num;
        
        if ( scanf ( " %c", &c ) != 1 )
            return FAILURE;
                
        if ( c == '}' )
            return SUCCESS;
        
        if ( c != ',' )
            return FAILURE;
        
    }
    
    if ( scanf ( " %c", &c ) == EOF )
        return FAILURE;
        
    if ( c == '}' )
        return SUCCESS;
    
    return FAILURE;
    
}

int main ( void ) {
    
    TREGION regions[27];
    printf ( "Zadejte data:\n" );
    
    for ( char c = 'A'; c <= 'Z'; c++ ) {
        regions[c - 'A'].num = 0;
        regions[c - 'A'].sum = 0;
    }
    
    if ( !loadRegions ( regions ) ) {
        printf ( "Nespravny vstup.\n" );
        return 1;
    }
    
    printf ( "Zadejte dotaz:\n" );
    scanf ( " " ); // remove trailing whitespace
    
    char * query = NULL;
    size_t querySize;
    
    if ( getline ( &query, &querySize, stdin ) == EOF ) {
        printf ( "Nespravny vstup.\n" );
        return 1;
    }
    
    query[strlen(query) - 1] = '\0';
    printf ( "Loaded: |%s|\n", query );
    
    if ( strlen ( query ) > 82 ) {
        printf ( "Nespravny vstup.\n" );
        free ( query );
        return 1;
    }
    
    char lastC = ' ', c;
    int num = 0;
    int sum = 0;
    
    char * queryIterated = query;
    while ( ( c = *queryIterated++ ) ) {
        
        if ( c == lastC )
            continue;
                
        if ( c == ' ' ) {
            
            if ( num == 0 )
                printf ( " = N/A\n" );
            else
                printf ( " = %f\n", sum / (double) num );
            
            num = sum = 0;
            lastC = ' ';
            continue;
            
        }
        
        if ( c < 'A' || c > 'Z' ) {
            printf ( "Nespravny vstup.\n" );
            free ( query );
            return 1;
        }
        
        num += regions[c - 'A'].num;
        sum += regions[c - 'A'].sum;
        lastC = c;
        
    }
    
    if ( num == 0 )
        printf ( " = N/A\n" );
    else
        printf ( " = %f\n", sum / (double) num );
    
    free ( query );
    
    return 0;
    
}
