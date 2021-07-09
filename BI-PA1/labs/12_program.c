#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct TSquare {
	int m_X;
	int m_Y;
	int m_Size;
} TSQUARE;

typedef struct TCanvasItem {
	struct TCanvasItem * m_Next;
	TSQUARE            * m_Square;
} TCANVASITEM;

TSQUARE * createSquare ( int x, int y, int size ) {
	TSQUARE * res = ( TSQUARE * ) malloc ( sizeof ( *res ) );
	res -> m_X    = x;
	res -> m_Y    = y;
	res -> m_Size = size;

	return res;
}

//Orderujeme primárně podle x, pak podle y
TCANVASITEM * addCanvasItem ( TCANVASITEM * list, TSQUARE * square ) {

  if ( list == NULL ) {

    list = ( TCANVASITEM * ) malloc ( sizeof (*list) );
    list->m_Next = NULL;
    list->m_Square = square;

    return list;

  }

  TCANVASITEM * tmp = list, * putAfter = NULL;

  while ( tmp ) {

    if ( tmp->m_Square->m_X < square->m_X || ( tmp->m_Square->m_X == square->m_X && tmp->m_Square->m_Y < square->m_Y ) ) { //pořadí zachováno
	putAfter = tmp;
    }

    if ( tmp->m_Next )
	tmp = tmp->m_Next;
    else
      break;

  }

  TCANVASITEM * newItem = ( TCANVASITEM * ) malloc ( sizeof (*newItem) );
  newItem->m_Square = square;

  //Dát na začátek seznamu
  if ( !putAfter ) {
    newItem->m_Next = list;
    return newItem;
  }

  //Dát do seznamu ZA prvek putAfter
  TCANVASITEM * originalNext = putAfter->m_Next;
  putAfter->m_Next = newItem;
  newItem->m_Next = originalNext;
  return list;
}

TCANVASITEM * delCanvasItem ( TCANVASITEM * list, int x, int y, int size, int * success ) {

  //Prázdný seznam
  if ( !list ) {
    *success = 0;
    return list;
  }

  TCANVASITEM * tmp, * previous;

  for ( tmp = list, previous = NULL; tmp; previous = tmp, tmp = tmp->m_Next )
    if ( tmp->m_Square->m_X == x && tmp->m_Square->m_Y == y && tmp->m_Square->m_Size == size )
	break;

  //Prvek nenalezen
  if ( !tmp ) {
    *success = 0;
    return list;
  }

  //Mažu ze začátku seznamu
  if ( !previous ) {
    TCANVASITEM * temp = tmp->m_Next;
    free ( tmp->m_Square );
    free ( tmp );
    *success = 1;
    return temp;
  }

  //Mažu z části / uprostřed seznamu
  previous->m_Next = tmp->m_Next;
  free ( tmp->m_Square );
  free ( tmp );
  *success = 1;
  return list;

}

void delCanvas ( TCANVASITEM * list ) {

  TCANVASITEM * previous, * tmp;

  for ( previous = NULL, tmp = list; tmp; previous = tmp, tmp = tmp->m_Next ) {

    if (previous)
      free ( previous->m_Square );

    free ( previous );

  }

  if ( previous )
    free ( previous->m_Square );

  free ( previous );

}

int main ( void ) {
	TCANVASITEM * a;
	int delRes;

	a = NULL;
	a = addCanvasItem ( a, createSquare ( 5, 5, 10 ) );
	assert ( a
	         && a -> m_Square
	         && a -> m_Square -> m_X == 5
	         && a -> m_Square -> m_Y == 5
	         && a -> m_Square -> m_Size == 10
	         && a -> m_Next == NULL );
	a = addCanvasItem ( a, createSquare ( 4, 5, 9 ) );
	assert ( a
	         && a -> m_Square
	         && a -> m_Square -> m_X == 4
	         && a -> m_Square -> m_Y == 5
	         && a -> m_Square -> m_Size == 9
	         && a -> m_Next
	         && a -> m_Next -> m_Square
	         && a -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Square -> m_Y == 5
	         && a -> m_Next -> m_Square -> m_Size == 10
	         && a -> m_Next -> m_Next == NULL );
	a = addCanvasItem ( a, createSquare ( 5, 1, 8 ) );
	assert ( a
	         && a -> m_Square
	         && a -> m_Square -> m_X == 4
	         && a -> m_Square -> m_Y == 5
	         && a -> m_Square -> m_Size == 9
	         && a -> m_Next
	         && a -> m_Next -> m_Square
	         && a -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Square -> m_Y == 1
	         && a -> m_Next -> m_Square -> m_Size == 8
	         && a -> m_Next -> m_Next
	         && a -> m_Next -> m_Next -> m_Square
	         && a -> m_Next -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Next -> m_Square -> m_Y == 5
	         && a -> m_Next -> m_Next -> m_Square -> m_Size == 10
	         && a -> m_Next -> m_Next -> m_Next == NULL );
	a = addCanvasItem ( a, createSquare ( 10, 10, 7 ) );
	assert ( a
	         && a -> m_Square
	         && a -> m_Square -> m_X == 4
	         && a -> m_Square -> m_Y == 5
	         && a -> m_Square -> m_Size == 9
	         && a -> m_Next
	         && a -> m_Next -> m_Square
	         && a -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Square -> m_Y == 1
	         && a -> m_Next -> m_Square -> m_Size == 8
	         && a -> m_Next -> m_Next
	         && a -> m_Next -> m_Next -> m_Square
	         && a -> m_Next -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Next -> m_Square -> m_Y == 5
	         && a -> m_Next -> m_Next -> m_Square -> m_Size == 10
	         && a -> m_Next -> m_Next -> m_Next
	         && a -> m_Next -> m_Next -> m_Next -> m_Square
	         && a -> m_Next -> m_Next -> m_Next -> m_Square -> m_X == 10
	         && a -> m_Next -> m_Next -> m_Next -> m_Square -> m_Y == 10
	         && a -> m_Next -> m_Next -> m_Next -> m_Square -> m_Size == 7
	         && a -> m_Next -> m_Next -> m_Next -> m_Next == NULL );
	delRes = -123;
	a = delCanvasItem ( a, 10, 10, 7, &delRes );
	assert ( delRes == 1
		     && a
	         && a -> m_Square
	         && a -> m_Square -> m_X == 4
	         && a -> m_Square -> m_Y == 5
	         && a -> m_Square -> m_Size == 9
	         && a -> m_Next
	         && a -> m_Next -> m_Square
	         && a -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Square -> m_Y == 1
	         && a -> m_Next -> m_Square -> m_Size == 8
	         && a -> m_Next -> m_Next
	         && a -> m_Next -> m_Next -> m_Square
	         && a -> m_Next -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Next -> m_Square -> m_Y == 5
	         && a -> m_Next -> m_Next -> m_Square -> m_Size == 10
	         && a -> m_Next -> m_Next -> m_Next == NULL );
	delRes = -123;
	a = delCanvasItem ( a, 5, 1, 8, &delRes );
	assert ( delRes == 1
		     && a
	         && a -> m_Square
	         && a -> m_Square -> m_X == 4
	         && a -> m_Square -> m_Y == 5
	         && a -> m_Square -> m_Size == 9
	         && a -> m_Next
	         && a -> m_Next -> m_Square
	         && a -> m_Next -> m_Square -> m_X == 5
	         && a -> m_Next -> m_Square -> m_Y == 5
	         && a -> m_Next -> m_Square -> m_Size == 10
	         && a -> m_Next -> m_Next == NULL );
	delRes = -123;
	a = delCanvasItem ( a, 4, 5, 9, &delRes );
	assert ( delRes == 1
		     && a
	         && a -> m_Square
	         && a -> m_Square -> m_X == 5
	         && a -> m_Square -> m_Y == 5
	         && a -> m_Square -> m_Size == 10
	         && a -> m_Next == NULL );
	a = addCanvasItem ( a, createSquare ( 10, 10, 7 ) );
	a = addCanvasItem ( a, createSquare ( 5, 1, 8 ) );
	a = addCanvasItem ( a, createSquare ( 4, 5, 9 ) );
	delCanvas ( a );
        delCanvas ( NULL );

	/* Zkontrolujte přes Valgrind uvolnění alokované paměti */

	return 0;
}
