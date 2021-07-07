#include<stdio.h>

void printRepeat ( char c, int times ) {
    for ( int i = 0; i < times; i++ )
        printf ( "%c", c );
}

void printHouse ( int width, int height ) {
    
    // Tisknu střechu
    for ( int indent = width / 2; indent > 0; indent-- ) {

        for ( int k = 0; k < width; k++ )
            if ( k == indent || k == width - 1 - indent )
                printf ( "X" );
            else
                printf ( " " );
        
        printf ( "\n" );
  
    }
    
    // Tisknu dům
    for ( int i = 1; i <= height; i++ ) {
        
        if ( i == 1 || i == height )
            printRepeat ( 'X', width );
        else {
            printf ( "X" );
            printRepeat ( ' ', width - 2 );
            printf ( "X" );
        }
        
        printf ( "\n" );
        
    }
    
}

void printHouseWithPlot ( int width, int height, int plot ) {
    
    // Tisknu střechu
    for ( int indent = width / 2; indent > 0; indent-- ) {

        for ( int k = 0; k < width; k++ )
              if ( k == indent || k == width - 1 - indent )
                  printf ( "X" );
              else
                  printf ( " " );
          
        printf ( "\n" );
    
    }
    
    // Tisk domu ! s plotem !
    for ( int i = 0; i < height; i++ ) {
        
        // Tisk domu
        if ( i == 0 || i == height - 1 )
            printRepeat ( 'X', width );
        else {
            for ( int j = 0; j < width; j++ ) {
                if ( j == 0 || j == width - 1 )
                    printf ( "X" );
                else
                    printf ( "%c", ( (i + j) % 2 == 0 ) ? 'o' : '*' );
            }
        }
        
        // Tisk plotu
        if ( i >= (height - plot) )
            for ( int j = 0; j < plot; j++ )
                printf ( "%c", j % 2 == (plot % 2) ? '-' : '|' );
        
        printf ( "\n" );
        
    }
    
}

int main ( void ) {
    
    int width, height, plot;
    
    if ( scanf ( "%d %d", &width, &height ) != 2 ) {
        fprintf ( stderr, "Error: Chybny vstup!\n" );
        return 100;
    }
    
    if ( width < 3 || height < 3 || width > 69 || height > 69 ) {
        fprintf ( stderr, "Error: Vstup mimo interval!\n" );
        return 101;
    }
    
    if ( width % 2 == 0 ) {
        fprintf ( stderr, "Error: Sirka neni liche cislo!\n" );
        return 102;
    }
    
    if ( width == height ) {
        
        if ( scanf ( "%d", &plot ) != 1 ) {
            fprintf ( stderr, "Error: Chybny vstup!\n" );
            return 100;
        }
        
        if ( plot >= height || plot <= 0 ) {
            fprintf ( stderr, "Error: Neplatna velikost plotu!\n" );
            return 103;
        }
        
        printHouseWithPlot ( width, height, plot );
        return 0;
        
    }
    
    printHouse ( width, height );
    return 0;
    
}
