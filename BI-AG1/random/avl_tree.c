#include "stdio.h"
#include "stdlib.h"

#define TYPE int

#define EQUAL 0
#define LBIGGER -1
#define RBIGGER 1
#define LBAD -2
#define RBAD 2

#define PREORDER -1
#define INORDER 0
#define POSTORDER 1

typedef struct TNode {
    TYPE m_Value;
    int m_Equality;
    
    struct TNode * m_Left;
    struct TNode * m_Right;
    struct TNode * m_Parent;
} TNODE;

int compareValue ( TYPE left, TYPE right ) {
    return ( left - right ) - ( right - left );
}

TNODE * createNode ( TYPE value ) {
    TNODE * node = (TNODE *) malloc ( sizeof(*node) );
    node->m_Value = value;
    node->m_Equality = EQUAL;
    node->m_Left = node->m_Right = node->m_Parent = NULL;
    return node;
}

void freeTree ( TNODE * node ) {
    if ( !node )
        return;

    // free ( node->m_Value );
    freeTree ( node->m_Left );
    freeTree ( node->m_Right );
    free ( node );
}

TNODE * searchNodeTree ( TNODE * node, TYPE value ) {
    if ( !node )
        return NULL;
    
    int cmp = compareValue ( value, node->m_Value );
    
    if ( cmp < 0 && node->m_Left )
        return searchNodeTree ( node->m_Left, value );
    
    if ( cmp > 0 && node->m_Right )
        return searchNodeTree ( node->m_Right, value );
    
    return node;
}

int searchTree ( TNODE * node, TYPE value ) {
    TNODE * result = searchNodeTree ( node, value );
    return result && compareValue ( value, result->m_Value ) == 0 ? 1 : 0;
}

void leftRotation ( TNODE ** rootPtr, TNODE * x, TNODE * y, TNODE * b ) {
    if ( b )
        b->m_Parent = x;
    
    y->m_Parent = x->m_Parent;
    if ( x->m_Parent ) {
        if ( x == x->m_Parent->m_Left )
            x->m_Parent->m_Left = y;
        else
            x->m_Parent->m_Right = y;
    }
    else
        *rootPtr = y;
    
    x->m_Parent = y;
    x->m_Left = b;
    y->m_Right = x;
    
    x->m_Equality = y->m_Equality = EQUAL;
    printf("Left rotation {%d, %d}\n", x->m_Value, y->m_Value);
}

void rightRotation ( TNODE ** rootPtr, TNODE * x, TNODE * y, TNODE * b ) {
    if ( b )
        b->m_Parent = x;
    
    y->m_Parent = x->m_Parent;
    
    if ( x->m_Parent ) {
        if ( x == x->m_Parent->m_Left )
            x->m_Parent->m_Left = y;
        else
            x->m_Parent->m_Right = y;
    }
    else
        *rootPtr = y;
    
    x->m_Parent = y;
    x->m_Right = b;
    y->m_Left = x;
    
    x->m_Equality = y->m_Equality = EQUAL;
    printf("Right rotation {%d, %d}\n", x->m_Value, y->m_Value);
}

void lrRotation ( TNODE ** rootPtr, TNODE * x, TNODE * y, TNODE * z, TNODE * b, TNODE * c ) {
    if ( b )
        b->m_Parent = y;
    if ( c )
        c->m_Parent = x;
    
    z->m_Parent = x->m_Parent;
    if ( x->m_Parent ) {
        if ( x == x->m_Parent->m_Left )
            x->m_Parent->m_Left = z;
        else
            x->m_Parent->m_Right = z;
    }
    else
        *rootPtr = z;
    
    x->m_Parent = z;
    y->m_Parent = z;
    
    z->m_Left = y;
    z->m_Right = x;
    
    y->m_Right = b;
    x->m_Left = c;
    
    x->m_Equality = ( z->m_Equality == LBIGGER ? RBIGGER : EQUAL );
    y->m_Equality = ( z->m_Equality == RBIGGER ? LBIGGER : EQUAL );
    z->m_Equality = EQUAL;
    
    printf ( "LR rotation {%d, %d, %d}\n", x->m_Value, y->m_Value, z->m_Value );
}

void rlRotation ( TNODE ** rootPtr, TNODE * x, TNODE * y, TNODE * z, TNODE * b, TNODE * c ) {
    if ( b )
        b->m_Parent = y;
    if ( c )
        c->m_Parent = x;
    
    z->m_Parent = x->m_Parent;
    if ( x->m_Parent ) {
        if ( x == x->m_Parent->m_Left )
            x->m_Parent->m_Left = z;
        else
            x->m_Parent->m_Right = z;
    }
    else
        *rootPtr = z;
    
    x->m_Parent = z;
    y->m_Parent = z;
    
    z->m_Right = y;
    z->m_Left = x;
    
    y->m_Left = b;
    x->m_Right = c;
    
    x->m_Equality = ( z->m_Equality == RBIGGER ? LBIGGER : EQUAL );
    y->m_Equality = ( z->m_Equality == LBIGGER ? RBIGGER : EQUAL );
    z->m_Equality = EQUAL;
    
    printf ( "RL rotation {%d, %d, %d}\n", x->m_Value, y->m_Value, z->m_Value );
}

void checkEqualityInsert ( TNODE ** rootPtr, TNODE * node ) {
    if ( node->m_Equality == EQUAL ) // Fine
        return;
    
    // Propagate
    if ( node->m_Equality == LBIGGER || node->m_Equality == RBIGGER ) {
        if ( !node->m_Parent )
            return;
        
        node->m_Parent->m_Equality += ( node->m_Parent->m_Left == node ? -1 : 1 );
        checkEqualityInsert ( rootPtr, node->m_Parent );
        return;
    }
    
    // Left rotation
    if ( node->m_Equality == LBAD && node->m_Left->m_Equality == LBIGGER ) {
        TNODE * b = node->m_Left->m_Right, * x = node, * y = node->m_Left;
        leftRotation ( rootPtr, x, y, b );
        return; // stop propagation
    }
    
    // Right rotation
    if ( node->m_Equality == RBAD && node->m_Right->m_Equality == RBIGGER ) {
        TNODE * b = node->m_Right->m_Left, * x = node, * y = node->m_Right;
        rightRotation ( rootPtr, x, y, b );
        return; // stop propagation
    }
    
    // LR rotation
    if ( node->m_Equality == LBAD && node->m_Left->m_Equality == RBIGGER ) {
        TNODE * x = node, * y = node->m_Left, * z = y->m_Right,
            * b = z->m_Left, * c = z->m_Right;
        lrRotation ( rootPtr, x, y, z, b, c );
        return; // stop propagation
    }
    
    // RL rotation
    if ( node->m_Equality == RBAD && node->m_Right->m_Equality == LBIGGER ) {
        TNODE * x = node, * y = node->m_Right, * z = y->m_Left,
            * b = z->m_Right, * c = z->m_Left;
        rlRotation ( rootPtr, x, y, z, b, c );
        return; // stop propagation
    }
    
    printf ( "Situation not covered!!!\n" );
}

void checkEqualityDelete ( TNODE ** rootPtr, TNODE * node ) {
    if ( node->m_Equality == EQUAL ) // Fine
        return;
    
    // Propagate
    if ( node->m_Equality == LBIGGER || node->m_Equality == RBIGGER ) {
        if ( !node->m_Parent )
            return;
        
        node->m_Parent->m_Equality += ( node->m_Parent->m_Left == node ? 1 : -1 );
        checkEqualityDelete ( rootPtr, node->m_Parent );
        return;
    }
    
    // Left rotation
    if ( node->m_Equality == LBAD && node->m_Left->m_Equality == LBIGGER ) {
        TNODE * b = node->m_Left->m_Right, * x = node, * y = node->m_Left;
        leftRotation ( rootPtr, x, y, b );
        return; // stop propagation
    }
    
    // Right rotation
    if ( node->m_Equality == RBAD && node->m_Right->m_Equality == RBIGGER ) {
        TNODE * b = node->m_Right->m_Left, * x = node, * y = node->m_Right;
        rightRotation ( rootPtr, x, y, b );
        return; // stop propagation
    }
    
    // Special delete case
    if ( node->m_Equality == RBAD && node->m_Right->m_Equality == EQUAL ) {
        TNODE * b = node->m_Right->m_Left, * x = node, * y = node->m_Right;
        rightRotation ( rootPtr, x, y, b );
        x->m_Equality = RBIGGER;
        y->m_Equality = LBIGGER;
        return; // stop propagation
    }
    
    if ( node->m_Equality == LBAD && node->m_Left->m_Equality == EQUAL ) {
        TNODE * b = node->m_Left->m_Right, * x = node, * y = node->m_Left;
        leftRotation ( rootPtr, x, y, b );
        x->m_Equality = LBIGGER;
        y->m_Equality = RBIGGER;
        return; // stop propagation
    }
    
    // LR rotation
    if ( node->m_Equality == LBAD && node->m_Left->m_Equality == RBIGGER ) {
        TNODE * x = node, * y = node->m_Left, * z = y->m_Right,
            * b = z->m_Right, * c = z->m_Left;
        lrRotation ( rootPtr, x, y, z, b, c );
        
        y->m_Equality = ( z->m_Equality == RBIGGER ? LBIGGER : EQUAL );
        x->m_Equality = ( z->m_Equality == LBIGGER ? RBIGGER : EQUAL );
        z->m_Equality = EQUAL;
        
        // propagate
        if ( !node->m_Parent )
            return;
        
        node->m_Parent->m_Equality += ( node->m_Parent->m_Left == node ? 1 : -1 );
        checkEqualityDelete ( rootPtr, node->m_Parent );
    }
    
    // RL rotation
    if ( node->m_Equality == RBAD && node->m_Right->m_Equality == LBIGGER ) {
        TNODE * x = node, * y = node->m_Right, * z = y->m_Left,
            * b = z->m_Left, * c = z->m_Right;
        rlRotation ( rootPtr, x, y, z, b, c );
        
        y->m_Equality = ( z->m_Equality == LBIGGER ? RBIGGER : EQUAL );
        x->m_Equality = ( z->m_Equality == RBIGGER ? LBIGGER : EQUAL );
        z->m_Equality = EQUAL;
        
        // propagate
        if ( !node->m_Parent )
            return;
        
        node->m_Parent->m_Equality += ( node->m_Parent->m_Left == node ? 1 : -1 );
        checkEqualityDelete ( rootPtr, node->m_Parent );
    }
}

int insertTree ( TNODE ** node, TYPE value ) {
    TNODE * where = searchNodeTree ( *node, value );
    if ( !where ) { // empty tree
        *node = createNode ( value );
        return 1;
    }
    
    int cmp = compareValue ( value, where->m_Value );
    if ( cmp == 0 ) // already exists
        return 0;
    
    if ( cmp < 0 ) {
        where->m_Left = createNode ( value );
        where->m_Left->m_Parent = where;
        where->m_Equality -= 1;
    }
    if ( cmp > 0 ) {
        where->m_Right = createNode ( value );
        where->m_Right->m_Parent = where;
        where->m_Equality += 1;
    }
    
    checkEqualityInsert ( node, where );
    return 1;
}

TNODE * findMin ( TNODE * node ) {
    if ( !node->m_Left )
        return node;
    return findMin ( node->m_Left );
}

TNODE * findSuccessor ( TNODE * node ) {
    if ( node->m_Right )
        return findMin ( node->m_Right );
    
    TNODE * succ = node->m_Parent;
    while ( succ && node == succ->m_Right ) {
        node = succ;
        succ = succ->m_Parent;
    }
    return succ;
}

int deleteTree ( TNODE ** node, TYPE value ) {
    TNODE * where = searchNodeTree ( *node, value );
    if ( !where ) // empty tree
        return 0;
    
    int cmp = compareValue ( value, where->m_Value );
    if ( cmp != 0 ) // not found
        return 0;
    
    // Both children
    if ( where->m_Left && where->m_Right ) {
        TNODE * successor = findSuccessor ( where );
        printf("Successor for %d is %d\n", where->m_Value, successor->m_Value);
        where->m_Value = successor->m_Value;
        where = successor;
    }
    
    // Leaf, just delete
    if ( !where->m_Left && !where->m_Right ) {
        TNODE * parent = where->m_Parent;
        
        // Root, unset and finish
        if ( !parent ) {
            freeTree ( where );
            *node = NULL;
            return 1;
        }
        
        if ( parent->m_Left == where ) {
            parent->m_Left = NULL;
            parent->m_Equality += 1;
        }
        else {
            parent->m_Right = NULL;
            parent->m_Equality -= 1;
        }
        // free ( where->m_Value );
        free ( where );
    
        checkEqualityDelete ( node, parent );
        return 1;
    }
    
    // One child, replace
    if ( where->m_Left && !where->m_Right ) {
        TNODE * parent = where->m_Parent;
        if ( parent ) {
            if ( parent->m_Left == where )
                parent->m_Left = where->m_Left;
            else
                parent->m_Right = where->m_Left;
        }
        
        where->m_Left->m_Parent = where->m_Parent;
        // free ( where->m_Value );
        free ( where );
        
        if ( parent )
            checkEqualityDelete ( node, parent );
        return 1;
    }
    if ( where->m_Right && !where->m_Left ) {
        TNODE * parent = where->m_Parent;
        if ( parent ) {
            if ( parent->m_Left == where )
                parent->m_Left = where->m_Right;
            else
                parent->m_Right = where->m_Right;
        }
        
        where->m_Right->m_Parent = where->m_Parent;
        // free ( where->m_Value );
        free ( where );
        
        if ( parent )
            checkEqualityDelete ( node, parent );
        return 1;
    }
    
    return 0;
    
}


void printTree ( TNODE * node, int order ) {
    if ( !node )
        return;
    if ( order == PREORDER )
        printf ( "%d ", node->m_Value );
    printTree ( node->m_Left, order );
    if ( order == INORDER )
        printf ( "%d ", node->m_Value );
    printTree ( node->m_Right, order );
    if ( order == POSTORDER )
        printf ( "%d ", node->m_Value );
}

int main ( void ) {
    TNODE * tree = NULL;
    char c; int num;
    
    printf ( "Zadejte prikaz (+ pridat, - odebrat, ? hledat, * print)\n" );
    while ( scanf(" %c %d", &c, &num) == 2 ) {
        switch ( c ) {
            case '+':
                printf("Insert %d (%s)\n", num, insertTree ( &tree, num ) ? "successful" : "failure");
                break;
            case '-':
                printf("Remove %d (%s)\n", num, deleteTree ( &tree, num ) ? "successful" : "failure");
                break;
            case '?':
                printf("Find %d (%s)\n", num, searchTree ( tree, num ) ? "successful" : "failure");
                break;
            case '*':
                printTree ( tree, num );
                printf("\n");
                break;
        }
    }
    
    printf ( "END.\n" );
    freeTree ( tree );
    return 0;
    
}
