#include<stdio.h>
#include<stdlib.h>
#include<string.h>

void nactiSlova ( char *** slova, int * pocetSlov );
int jePalindrom ( char * slovo );
void spocitejPalindromy ( char ** slova, int pocetSlov, int * palindromu );
void spocitejZnaky ( char ** slova, int pocetSlov, int * znaku );
void zjistiJestliKonciDobre ( char ** slova, int pocetSlov, int * konciDobre );
void uvolniSlova ( char ** slova, int pocetSlov );

int main(void) {
        
    printf("Zadejte souveti:\n");
    
    char ** slova;
    int pocetSlov, pocetPalindromu, pocetZnaku, konciDobre;
    
    nactiSlova ( &slova, &pocetSlov );
    spocitejPalindromy ( slova, pocetSlov, &pocetPalindromu );
    spocitejZnaky ( slova, pocetSlov, &pocetZnaku );
    zjistiJestliKonciDobre ( slova, pocetSlov, &konciDobre );
    
    printf( "\n" );
    printf( "Počet slov: %d\n", pocetSlov );
    printf( "Počet palindromů: %d\n", pocetPalindromu );
    printf( "Počet znaků: %d\n", pocetZnaku );
    printf( "Souvětí je ukončeno tečkou, otazníkem, vykřičníkem: %s\n",
           konciDobre ? "ano" : "ne" );
    
    uvolniSlova ( slova, pocetSlov );
    
    return 0;
    
}

void nactiSlova ( char *** slova, int * pocetSlov ) {
    
    char c, znak, * slovo = NULL, * radka = NULL, ** tmp = NULL;
    size_t velikost;
    int pocet = 0, delkaSlova = 0, maxDelkaSlova = 0, mezera = 1;
    
    do {
      c = getline( &radka, &velikost, stdin );
      if (c == EOF) {
        free(radka);
        radka = malloc ( 2 * sizeof(*radka) );

	radka[0] = '\n';
	radka[1] = '\0';
      }
    
      for ( char * p = radka; *p; p++ ) {
        
	  znak = *p;
        
          if ( znak == '\n' || znak == ' ' ) {
            
            if ( !mezera ) {
                
                tmp = (char **) realloc ( tmp, ( pocet + 1 ) * sizeof( *tmp ) );
                tmp [ pocet++ ] = slovo;
                
                slovo = NULL;
                delkaSlova = 0;
                maxDelkaSlova = 0;
                
            }
            
            mezera = 1;

          }
          else {
            
            if ( delkaSlova >= maxDelkaSlova ) {
                
                maxDelkaSlova += ( maxDelkaSlova < 8 ) ? 64 : ( maxDelkaSlova / 2 );
                slovo = (char *) realloc ( slovo, maxDelkaSlova * sizeof( *slovo ) );
                
            }
            
            slovo [ delkaSlova++ ] = znak;
            slovo [ delkaSlova ] = '\0';
            mezera = 0;
            
          }
        
      }

    }
    while (c != EOF);
    
    *slova = tmp;
    *pocetSlov = pocet;
    
    free ( radka );

}

int jePalindrom ( char * slovo ) {
    
    int delkaSlova = strlen ( slovo );
    char * pulkaSlova = slovo + ( delkaSlova / 2 );
        
    for ( char * a = slovo, * b = slovo + (delkaSlova - 1); a != pulkaSlova; a++, b-- )
        if ( *a != *b )
            return 0;
    
    return 1;
    
}

void spocitejPalindromy ( char ** slova, int pocetSlov, int * palindromu ) {
    
    int pocetPalindromu = 0;
    
    for ( int i = 0; i < pocetSlov; i++ ) {
        
        if ( jePalindrom ( slova[i] ) )
            pocetPalindromu++;
                
    }
    
    *palindromu = pocetPalindromu;
    
}

void spocitejZnaky ( char ** slova, int pocetSlov, int * znaku ) {
    
    int pocetZnaku = 0;
    
    for ( int i = 0; i < pocetSlov; i++ )
        pocetZnaku += strlen ( slova[i] );
    
    *znaku = pocetZnaku;
    
}

void zjistiJestliKonciDobre ( char ** slova, int pocetSlov, int * konciDobre ){
    
    if ( pocetSlov == 0 ) {
        *konciDobre = 0;
        return;
    }
    
    char * posledniSlovo = slova[pocetSlov - 1];
    char posl = posledniSlovo [ ( strlen ( posledniSlovo ) - 1 ) ] ;
    
    *konciDobre = ( posl == '.' || posl == '!' || posl == '?' );
    
}

void uvolniSlova ( char ** slova, int pocetSlov ) {
    
    for ( int i = 0; i < pocetSlov; i++ )
        free ( slova[i] );
    
    free ( slova );
    
}
