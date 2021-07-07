#include<ctype.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef struct TCar {
    int valid;
    int timestampPrijezd;
    char SPZ[11]; //10 znaků + '\0'
} TCAR;

/*
 * Program pro počítání poplatků za parkování
 * na vstupu jsou zadávány příkazy ve formátu:
 *
 * 15:20 + AUTICKO
 * 15:40 - MOTOVOZ
 * 00:00 =
 *
 * Kde je vždy čas, příkaz + (příjezd), - (odjezd) nebo = (nový den)
 * Časy musí jít vždy po sobě (s výjimkou 0:00 =), SPZ smí mít max. 10 znaků
 *
 * Program vypíše platnou cenu parkování dle ceníku:
 * 0 Kč do 30min, 30 Kč do 60min, do 1 dne pak 40 Kč za každou započatou hodinu,
 * v případě delší doby 1000 Kč za každý započatý den.
 */
int main ( void ) {
    
    TCAR * parkoviste = ( TCAR * ) malloc ( 16 * sizeof ( *parkoviste ) );
    char * radek = NULL;
    size_t velikostRadku;
    int startTimestamp = 0, pocetAut = 0, maxPocet = 16, realnyPocet = 0;
    
    printf ( "Log:\n" );
    int lastTimestamp = 0;
    
    while ( getline ( &radek, &velikostRadku, stdin ) != EOF ) {
        
        radek[strlen(radek) - 1] = '\0';
        
        int hour, minute, pos;
        char c1, c2;
        
        // Neplatný vstup
        if ( sscanf ( radek, "%d%c%d %c %n", &hour, &c1, &minute, &c2, &pos ) < 4 ||
            hour < 0 || hour > 23 || minute < 0 || minute > 59 ||
            c1 != ':' || ( c2 != '+' && c2 != '-' && c2 != '=' ) ||
            ( ( hour * 60 + minute ) < lastTimestamp && c2 != '=' ) ) {
            
            printf ( "Nespravny vstup.\n" );
            free ( radek );
            free ( parkoviste );
            return 1;
            
        }
        
        lastTimestamp = hour * 60 + minute;
        char * radekPointer = radek + pos, * pomocny = radek + pos;
        
        while ( *pomocny )
            if ( !isblank ( *pomocny ) )
                pomocny++;
            else
                pomocny[0] = '\0';
                
        if ( c2 == '=' ) {
            
            if ( lastTimestamp != 0 || strlen ( radekPointer ) != 0 ) {
                
                printf ( "Nespravny vstup.\n" );
                free ( radek );
                free ( parkoviste );
                return 1;
                
            }
            
            startTimestamp += 1440;
            continue;
            
        }
        
        // + nebo -
        if ( strlen ( radekPointer ) > 10 ) {
            
            printf ( "Nespravny vstup.\n" );
            free ( radek );
            free ( parkoviste );
            return 1;
            
        }
        
        if ( pocetAut + 1 > maxPocet ) {
            
            maxPocet += ( maxPocet < 32 ? 64 : maxPocet / 2 );
            parkoviste = ( TCAR * ) realloc ( parkoviste, maxPocet * sizeof ( *parkoviste ) );
            
        }
        
        if ( c2 == '+' ) {
            
            parkoviste[pocetAut].valid = 1;
            parkoviste[pocetAut].timestampPrijezd = startTimestamp + hour * 60 + minute;
            strncpy ( parkoviste[pocetAut].SPZ, radekPointer, 10 );
            
            printf ( "OK.\n" );
            pocetAut++;
            realnyPocet++;
            
        }
        
        else {
            
            int index = -1;
            
            for ( int i = 0; i < pocetAut; i++ )
                if ( parkoviste[i].valid && strncmp ( parkoviste[i].SPZ, radekPointer, 10 ) == 0 ) {
                    index = i;
                    break;
                }
            
            if ( index == -1 )
                printf ( "Nenalezeno.\n" );
            else {
                
                parkoviste[index].valid = 0;
                           
                int cena = -1;
                int timePassed = ( startTimestamp + hour * 60 + minute ) - parkoviste[index].timestampPrijezd;
                
                if ( timePassed <= 30 )
                    cena = 0;
                else if ( timePassed <= 60 )
                    cena = 30;
                else if ( timePassed <= 1440 )
                    cena = ( ( timePassed / 60 ) + ( timePassed % 60 == 0 ? 0 : 1 ) ) * 40;
                else
                    cena = ( ( timePassed / 1440 ) + ( timePassed % 1440 == 0 ? 0 : 1 ) ) * 1000;
                
                printf ( "RZ: %s, cena: %d\n", parkoviste[index].SPZ, cena );
                realnyPocet--;
                
            }
            
        }
        
    }
    
    free ( radek );
    free ( parkoviste );
    printf ( "Pocet automobilu v garazi: %d\n", realnyPocet );
    
    return 0;
    
}
