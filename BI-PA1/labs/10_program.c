#include<stdio.h>
#include<stdlib.h>
#include<ctype.h>

void lower ( char * text ) {
  text--;
  while ( *text++ )
     *text = tolower ( *text );
}

void upper ( char * text ) {
  text--;
  while ( *text++ )
     *text = toupper ( *text );
}

int main ( void ) {

  char * originalLine, *line;
  size_t size;

  if ( getdelim ( &originalLine, &size, '\n', stdin ) < 2 ) {
    printf("Nespravny vstup.\n");
    return 1;
  }

  line = originalLine;
  char action, space;
  int remains;

  if ( sscanf ( line, " %c%c%n", &action, &space, &remains ) < 2 || space != ' ' ) {
    printf("Nespravny vstup.\n");
    free ( line );
    return 1;
  }

  line += remains;

  switch ( action ) {

    case 'n':
      break;

    case 'l':
      lower ( line );
      break;

    case 'u':
      upper ( line );
      break;

    default:
      printf("Nespravny vstup.\n");
      free ( line );
      return 1;

  }

  printf( "%s" , line );
  free ( originalLine );
  return 0;

}
