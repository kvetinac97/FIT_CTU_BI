#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef struct {
  char   letter;
  int    number;
  int    people;
  int  departed;
} TBUS;

int main ( void ) {

  printf ("Zadejte pocet zaznamu:\n");
  int n;

  if ( scanf ( "%d", &n ) != 1 ) {
    printf ("Nespravny vstup.\n");
    return 1;
  }

  TBUS * busses = (TBUS *) malloc ( n * sizeof (*busses) );
  char * radek = NULL;
  size_t radekSize;

  int writeI = 0;
  for ( int i = 0; i < n; i++ ) {

    if ( getline ( &radek, &radekSize, stdin ) == EOF ) {
	printf ("Nespravny vstup.\n");
	free ( radek );
	free ( busses );
	return 1;
    }

    radek[strlen(radek) - 1] = '\0'; //Odstraníme newline
    char plusMinus, letter, semicolon;
    int n1, n2;

    if ( scanf ( " %c %c%d %c%d", &plusMinus, &letter, &n1, &semicolon, &n2 ) != 5
	|| ( plusMinus != '+' && plusMinus != '-' ) || semicolon != ':' || n1 < 0 || n1 > 99
	|| n2 < 0 ) {

	printf ("Nespravny vstup.\n");
	free ( radek );
	free ( busses );
	return 1;

    }

    int realBusI = -1;
    for ( int j = 0; j < writeI; j++ ) {
      if ( busses[j].letter == letter && busses[j].number == n1 ) {
	 realBusI = j;
	 break;
      }
    }

    if ( realBusI == -1 ) {
      TBUS bus;
      bus.letter = letter;
      bus.number = n1;
      bus.people = 0;
      bus.departed = 0;
      busses[writeI] = bus;
      realBusI = writeI++;
    }

    if ( plusMinus == '+' )
      busses[realBusI].people += n2;

    if ( plusMinus == '-' ) {
      if ( busses[realBusI].people >= n2 ) {
	busses[realBusI].people -= n2;
	busses[realBusI].departed += n2;
      }
      else {
	busses[realBusI].departed += busses[realBusI].people;
	busses[realBusI].people = 0;
      }
    }

  }

  printf ("Celkem odjelo {nastupiste,lide}: ");
  int necoVypsano = 0, last = -1;

  for ( int i = 0; i < writeI; i++ )
    if ( busses[i].departed > 0 ) {
      necoVypsano = 1;
      last = i;
    }

  for ( int i = 0; i < writeI; i++ ) {
    if ( busses[i].departed <= 0 )
      continue;
    printf ("{%c%d,%d}", busses[i].letter, busses[i].number, busses[i].departed);
    if ( i == last - 1 )
      printf (", ");
    else
      printf (".\n");
    necoVypsano = 1;
  }

  if ( !necoVypsano )
    printf ("0.\n");

  free ( radek );
  free ( busses );
  return 0;

}
