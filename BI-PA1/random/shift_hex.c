#include<stdio.h>
#include<stdlib.h>
#include<assert.h>

/*
 * Funkce pro bitový posun šestnáckových čísel zapsaných jako spojový seznam
 * Číslo je vráceno opět jako šestnáckové zapsáno jako spojový seznam
 */
typedef struct TNode {
    char m_Digit;
    struct TNode * m_Next;
} TNODE;

TNODE * createNode ( char digit, TNODE * next ) {
    TNODE * n = ( TNODE * ) malloc ( sizeof ( *n ) );
    n -> m_Digit = digit;
    n -> m_Next = next;
    return n;
}
 
void deleteList (TNODE * l) {
    while (l) {
        TNODE * tmp = l -> m_Next;
        free (l);
        l = tmp;
    }
}

int transferNodeToNum ( TNODE * a ) {
    
    int mocnina16 = 1, cislo = 0;
        
    for ( TNODE * node = a; node; node = node->m_Next ) {
        
        char c = node->m_Digit;
        if ( ! ( ( c >= '0' && c <= '9' ) || ( c >= 'a' && c <= 'f' ) ) ||
            ( cislo != 0 && c == '0' && !node->m_Next ) )
            return -1;
        
        cislo += ( c >= '0' && c <= '9' ? c - '0' : c - 'a' + 10 ) * mocnina16;
        mocnina16 *= 16;
        
    }
    
    return cislo;
    
}

TNODE * transferNumToNode ( int num, int mocnina ) {
    
    if ( num <= 0 && mocnina > 1 )
        return NULL;
    
    int cis = ( num % ( 16 * mocnina ) ) / mocnina;
    char c = ( cis < 10 ? '0' + cis : 'a' + ( cis - 10 ) );
    return createNode ( c, transferNumToNode ( num - cis * mocnina, mocnina * 16 ) );
    
}

TNODE * shiftLeft ( TNODE * a, unsigned int shiftLeft ) {
    
    if ( !a ) // neplatné číslo
        return NULL;
    
    int num = transferNodeToNum ( a );
    if ( num == -1 )
        return NULL;
    
    int shiftedNum = ( num << shiftLeft );
    return transferNumToNode ( shiftedNum, 1 );
    
}

int main (int argc, char * argv [])
{
        
    TNODE * a = NULL, * res = NULL;
    a = createNode ( '3',
          createNode ( '2',
            createNode ( '1', NULL )));
    res = shiftLeft ( a, 1 );
    assert ( res -> m_Digit == '6' );
    assert ( res -> m_Next -> m_Digit == '4' );
    assert ( res -> m_Next -> m_Next -> m_Digit == '2' );
    assert ( res -> m_Next -> m_Next -> m_Next == NULL );
    deleteList ( a );
    deleteList ( res );
     
    a = createNode ( 'f',
          createNode ( 'a',
            createNode ( '1', NULL )));
    res = shiftLeft ( a, 3 );
    assert ( res -> m_Digit == '8' );
    assert ( res -> m_Next -> m_Digit == '7' );
    assert ( res -> m_Next -> m_Next -> m_Digit == 'd' );
    assert ( res -> m_Next -> m_Next -> m_Next == NULL );
    deleteList ( a );
    deleteList ( res );
     
    a = createNode ( 'c',
          createNode ( '5',
            createNode ( '4',
              createNode ( 'd',
                createNode ( '1', NULL )))));
    res = shiftLeft ( a, 5 );
    assert ( res -> m_Digit == '0' );
    assert ( res -> m_Next -> m_Digit == '8' );
    assert ( res -> m_Next -> m_Next -> m_Digit == 'b' );
    assert ( res -> m_Next -> m_Next -> m_Next -> m_Digit == '8' );
    assert ( res -> m_Next -> m_Next -> m_Next -> m_Next -> m_Digit == 'a' );
    assert ( res -> m_Next -> m_Next -> m_Next -> m_Next -> m_Next -> m_Digit == '3' );
    assert ( res -> m_Next -> m_Next -> m_Next -> m_Next -> m_Next -> m_Next == NULL );
    deleteList ( a );
    deleteList ( res );
     
    a = createNode ( 'a',
          createNode ( 'w',
            createNode ( '0', NULL )));
    res = shiftLeft ( a, 12 );
    assert ( res == NULL );
    deleteList ( a );
     
    a = NULL;
    res = shiftLeft ( a, 1 );
    assert ( res == NULL );
    return 0;
}
