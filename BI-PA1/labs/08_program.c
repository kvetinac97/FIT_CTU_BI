#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

#define MODRY_HRAC     1
#define ORANZOVY_HRAC -1
#define NEOBSAZENO     0
 
/* Funkce vrací 0, pokud je požadovaný tah neplatný. */
int tah( int hraciDeska[5][5], int hrac, int radek, char sloupec, int cilovyRadek, char cilovySloupec ) {

  if (radek < 1 || radek > 5 || cilovyRadek < 1 || cilovyRadek > 5)
    return 0;

  sloupec = tolower(sloupec);
  cilovySloupec = tolower(cilovySloupec);

  if (sloupec < 'a' || sloupec > 'e' || cilovySloupec < 'a' || cilovySloupec > 'e')
    return 0;

  if (hraciDeska[radek-1][sloupec-97] != hrac)
    return 0;

  if (hraciDeska[cilovyRadek-1][cilovySloupec-97] != 0)
    return 0;

  if (abs(cilovyRadek-radek) != 1 || abs(cilovySloupec-sloupec) != 1)
    return 0;

  hraciDeska[cilovyRadek-1][cilovySloupec-97] = hraciDeska[radek-1][sloupec-97];
  hraciDeska[radek-1][sloupec-97] = 0;
  return 1;

}

void vytiskniDesku(int hraciDeska[5][5]){
   printf("  A B C D E\n");
   for (int i = 0; i < 5; i++) {
     printf("%d ", i+1);
     for (int j = 0; j < 5; j++)
	printf("%c ", hraciDeska[i][j] == 1 ? 'M' : (hraciDeska[i][j] == -1 ? 'R' : 'O'));
     printf("\n");
   }
}

int main( void ) {
	int hraciDeska[5][5] = {0};
	int aktualniHrac = MODRY_HRAC,
	    vysledekNacteni,
	    radek,
	    cilovyRadek;
	char sloupec,
	     cilovySloupec;

	for (int i = 0; i < 5; i++)
	  hraciDeska[0][i] = 1;

	for (int i = 0; i < 5; i++)
	  hraciDeska[4][i] = -1;

	vytiskniDesku(hraciDeska);

	while ( 1 ) {
		printf( "Na rade je %s hrac. Zadejte pocatecni cislo radku a pismeno sloupce a take cilove cislo radku a pismeno sloupce:\n",
			    aktualniHrac == MODRY_HRAC ? "modry" : "oranzovy" );

		vysledekNacteni = scanf( "%d %c %d %c", &radek, &sloupec, &cilovyRadek, &cilovySloupec );
		if ( vysledekNacteni == EOF ) {
			printf( "Ukoncili jste hru.\n" );
			return 0;
		}

		if ( vysledekNacteni != 4 || tah( hraciDeska, aktualniHrac, radek, sloupec, cilovyRadek, cilovySloupec ) != 1 ) {
			printf( "Neplatny vstup. Zkuste to znovu.\n" );
			char c = 0;
			do {
			  c = fgetc(stdin);
			}
			while (c != EOF && c != '\n');
			continue;
		}

		int vyhralModry = 1, vyhralCerveny = 1;
		for (int i = 0; i < 5; i++)
		  if (hraciDeska[0][i] != -1)
		    vyhralCerveny = 0;

		for (int i = 0; i < 5; i++)
		  if (hraciDeska[4][i] != 1)
		    vyhralModry = 0;

		if (vyhralModry || vyhralCerveny)
		  break;

		/* Výhodné mít hodnoty '1' a '-1'. */
		aktualniHrac *= -1;

		vytiskniDesku(hraciDeska);
	}

	printf("Vyhral %s.\n", aktualniHrac == MODRY_HRAC ? "modry" : "oranzovy");

	return 0;
}
