#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    int value;
    struct node * next;
} node_t;

void nodeCreate ( node_t ** head )
{
    *head = NULL;
}

void nodePushBackRecursive ( node_t ** head, int value )
{
    if ( *head != NULL ) {
        nodePushBackRecursive( &( ( *head ) -> next ), value );
    } else {
        *head = (node_t *) malloc( sizeof( node_t ) );
        ( *head ) -> value = value;
        ( *head ) -> next = NULL;
    }
}

void nodePrintRecursive ( const node_t * head )
{
    if ( head == NULL ) {
        printf( "\n" );
        return;
    }

    printf( "-> %d ", head -> value );
    nodePrintRecursive( head -> next );
}

void nodeDeleteRecursive ( node_t * head )
{
    if ( head != NULL ) {
        nodeDeleteRecursive( head -> next );
    }

    free( head );
}

int main ( void )
{
    node_t * n;

    nodeCreate( &n );
    nodePushBackRecursive( &n, 1 );
    nodePushBackRecursive( &n, 2 );
    nodePushBackRecursive( &n, 3 );
    nodePushBackRecursive( &n, 4 );
    nodePrintRecursive( n );
    nodeDeleteRecursive( n );

    return 0;
}

