#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#define MAX_SIZE 20

/*
 * Program řešící osmisměrku o maximální velikosti 20x20
 * osmisměrka je zadána na standardním vstupu ve formátu:
 *
 * alsakfdjkldajg
 * klfajklsdjflkc
 * fklajlfjallkfj
 *
 * Tvar nemusí být obdélníkový, může být libovolně nepravidelný.
 * Program vypíše vyškrtanou osmisměrku.
 */
void nactiOsmismerku ( char pole[][MAX_SIZE + 1] ) {
    
    printf ("Osmismerka:\n");
    
    char * radek = NULL;
    size_t radekSize;
    int eof = 0;
    
    for ( int i = 0; i < MAX_SIZE; i++ ) {
                
        // Empty line or EOF
        if ( eof || getdelim ( &radek, &radekSize, '\n', stdin ) <= 1 ) {
            
            for ( int j = 0; j <= MAX_SIZE; j++ )
                pole[i][j] = '\0';
            
            eof = 1;
            continue;
            
        }
        
        int len = strlen ( radek );
        
        if ( radek[len - 1] == '\n' )
            radek[len - 1] = '\0';
        
        strncpy ( pole[i], radek, MAX_SIZE );
        pole[i][MAX_SIZE] = '\0';
        
    }
    
    free ( radek );
    
}

int vyskrtejCast ( char * cast, char * slovo, int skrtat[MAX_SIZE], int vyskrtane[][MAX_SIZE] ) {
    
    if ( cast[0] == '\0' )
        return 0;
    
    char * res = strstr ( cast, slovo );
    int nalezeno = 0, start = res - cast;
    
    if ( res != NULL )
        for ( int i = start; i < start + strlen ( slovo ); i++ ) {

            if ( i >= MAX_SIZE )
                break;
            
            if ( skrtat[i] < 0 )
                continue;
            
            vyskrtane[skrtat[i] / MAX_SIZE][skrtat[i] % MAX_SIZE] = 1;
            nalezeno = 1;
        }
    
    return nalezeno;
    
}

int vyskrtejSlovo ( char pole[][MAX_SIZE + 1], int vyskrtane[][MAX_SIZE], char * slovo ) {
    
    int slovoLen = strlen ( slovo );
    int nalezeno = 0;
    
    if ( slovo[slovoLen - 1] == '\n' ) {
        slovo[slovoLen - 1] = '\0';
        slovoLen--;
    }
        
    if ( slovoLen > MAX_SIZE )
        return 0;
        
    char part[MAX_SIZE + 1];
    int partSkrtat[MAX_SIZE];
    
    for ( int i = 0; i < MAX_SIZE; i++ ) {
        part[i] = '\0';
        partSkrtat[i] = -1;
    }
    part[MAX_SIZE] = '\0';
    
    for ( int i = 0; i < MAX_SIZE; i++ ) {
        
        for ( int j = 0; j < MAX_SIZE; j++ )
            partSkrtat[j] = i * MAX_SIZE + j;
        
        nalezeno |= vyskrtejCast ( pole[i], slovo, partSkrtat, vyskrtane );
        
        int ix = 0;
        for ( int j = MAX_SIZE - 1; j >= 0; j-- ) {
            
            if ( pole[i][j] == '\0' )
                continue;
            
            part[ix] = pole[i][j];
            partSkrtat[ix++] = i * MAX_SIZE + j;
            
        }
        
        part[ix++] = '\0';
        nalezeno |= vyskrtejCast ( part, slovo, partSkrtat, vyskrtane );
        
    }
    
    for ( int j = 0; j < MAX_SIZE; j++ ) {
        
        int ix = 0;
        for ( int i = 0; i < MAX_SIZE; i++ ) {
            
            if ( pole[i][j] == '\0' )
                continue;
            
            part[ix] = pole[i][j];
            partSkrtat[ix++] = i * MAX_SIZE + j;
            
        }
        
        part[ix] = '\0';
        nalezeno |= vyskrtejCast ( part, slovo, partSkrtat, vyskrtane );
        
        ix = 0;
        for ( int i = MAX_SIZE - 1; i >= 0; i-- ) {
            
            if ( pole[i][j] == '\0' )
                continue;
            
            part[ix] = pole[i][j];
            partSkrtat[ix++] = i * MAX_SIZE + j;
            
        }
        
        part[ix] = '\0';
        nalezeno |= vyskrtejCast ( part, slovo, partSkrtat, vyskrtane );
        
    }
    
    for ( int i = 0; i < MAX_SIZE; i++) {
        for ( int j = 0; j < MAX_SIZE; j++ ) {
            
            // Z každého bodu cesta doprava dolů
            int ix = 0;
            
            for ( int add = 0; i + add < MAX_SIZE && j + add < MAX_SIZE; add++ ) {
                
                if ( pole[i + add][j + add] == '\0' )
                    continue;
                
                part[ix] = pole[i + add][j + add];
                partSkrtat[ix++] = ( i + add ) * MAX_SIZE + j + add;
                
            }
            
            part[ix] = '\0';
            nalezeno |= vyskrtejCast ( part, slovo, partSkrtat, vyskrtane );
            
            // Z každého bodu cesta doleva dolů
            ix = 0;
            
            for ( int add = 0; i + add < MAX_SIZE && j - add >= 0; add++ ) {
                
                if ( pole[i + add][j - add] == '\0' )
                    continue;
                
                part[ix] = pole[i + add][j - add];
                partSkrtat[ix++] = ( i + add ) * MAX_SIZE + ( j - add );
                
            }
            
            part[ix] = '\0';
            nalezeno |= vyskrtejCast ( part, slovo, partSkrtat, vyskrtane );
            
            // Z každého bodu cesta doprava dolů obráceně
            ix = 0;
            
            for ( int remove = 0; i - remove >= 0 && j - remove >= 0; remove++ ) {
                
                if ( pole[i - remove][j - remove] == '\0' )
                    continue;
                
                part[ix] = pole[i - remove][j - remove];
                partSkrtat[ix++] = ( i - remove ) * MAX_SIZE + ( j - remove );
                
            }
            
            part[ix] = '\0';
            nalezeno |= vyskrtejCast ( part, slovo, partSkrtat, vyskrtane );
            
            // Z každého bodu cesta doleva dolů obráceně
            ix = 0;
            
            for ( int remove = 0; i - remove >= 0 && j + remove < MAX_SIZE; remove++ ) {
                
                if ( pole[i - remove][j + remove] == '\0' )
                    continue;
                
                part[ix] = pole[i - remove][j + remove];
                partSkrtat[ix++] = ( i - remove ) * MAX_SIZE + ( j + remove );
                
            }
            
            part[ix] = '\0';
            nalezeno |= vyskrtejCast ( part, slovo, partSkrtat, vyskrtane );
            
        }
    }
        
    return nalezeno;
    
}

void vypisZbylaPismena ( char pole[][MAX_SIZE + 1], int vyskrtane[][MAX_SIZE] ) {
    
    printf ( "Vyskrtana osmismerka:\n" );
    
    for ( int i = 0; i < MAX_SIZE; i++ )
        for ( int j = 0; j < MAX_SIZE; j++ )
            if ( ! vyskrtane[i][j] && pole[i][j] != '\0' )
                printf ( "%c", pole[i][j] );
    
    printf ( "\n" );
    
}

void vyskrtejOsmismerku ( char pole[][MAX_SIZE + 1], int vyskrtane[][MAX_SIZE] ) {
    
    printf ("Slova k vyskrtani:\n");
    
    char * radek = NULL;
    size_t radekSize;
    
    while ( getdelim ( &radek, &radekSize, '\n', stdin ) != EOF ) {
        
        int f = vyskrtejSlovo ( pole, vyskrtane, radek );
        if ( !f )
            printf ( "%s\n", radek );
        
    }
    
    free ( radek );
    
}

int main ( void ) {
    
    char pole[MAX_SIZE][MAX_SIZE + 1];
    int vyskrtane[MAX_SIZE][MAX_SIZE];
    
    nactiOsmismerku ( pole );
    
    for ( int i = 0; i < MAX_SIZE; i++ )
        for ( int j = 0; j < MAX_SIZE; j++ )
            vyskrtane[i][j] = 0;
    
    vyskrtejOsmismerku ( pole, vyskrtane );
    vypisZbylaPismena ( pole, vyskrtane );
    
    return 0;
    
}
