#include <stdio.h>
#include <stdlib.h>

#define INIT_SIZE 4

void interSet(int * set1, int setSize1, int * set2, int setSize2) {
    int * set1Last = set1 + setSize1 - 1;
    int * set2Last = set2 + setSize2 - 1;
    while(1) {
        if (*set1 == *set2) {
            printf("%d ", *set1);
            if (set1 != set1Last)
                set1++;
            else return;
            if (set2 != set2Last)
                set2++;
            else return;
        }
        else if (*set1 < *set2) {
            if (set1 != set1Last)
                set1++;
            else return;
        }
        else {
            if (set2 != set2Last)
                set2++;
            else return;
        }
    }
}

int * readSet ( int * setSize ) {
    int res, size = 0, maxSize = INIT_SIZE;
    int * set = (int *) malloc(maxSize * sizeof(*set));
    
    while ((res = scanf("%d", &set[size])) != EOF){
        if (res != 1 || (size > 0 && set[size - 1] >= set[size])) {
            free(set);
            return NULL;
        }
        
        size++;
        
        if (size == maxSize) {
            maxSize += (maxSize < 10 ? 100 : maxSize / 2);
            int * newSet = realloc(set, maxSize * sizeof(*set));
            
            if (newSet == NULL){
                free(set);
                return NULL;
            }
            
            set = newSet;
        }
    }
    
    *setSize = size;
    return set;
}

void printSet(int * set, int setSize) {
    for (int i = 0; i < setSize; i++)
        printf("%d ", set[i]);
    printf("\n");
}

int main( void ){
    
    int setSize1, setSize2, * set1, * set2;
    
    printf("Mnozina 1:\n");
    if ((set1 = readSet(&setSize1)) == NULL){
        printf("Nespravny vstup.\n");
        //return 1;
    }
    
    printf("Mnozina 1 vypis:\n");
    printSet(set1, setSize1);
    
    printf("Mnozina 2:\n");
    if ((set2 = readSet(&setSize2)) == NULL){
        printf("Nespravny vstup.\n");
	free(set1);
        //return 1;
    }
    
    printf("Mnozina 2 vypis:\n");
    printSet(set2, setSize2);

    interSet(set1, setSize1, set2, setSize2);
    printf("\n");
    
    free(set1);
    free(set2);
    return 0;
}
