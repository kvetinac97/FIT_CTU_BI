#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<math.h>
#include<float.h>

typedef struct TPlane {
    
    double x;
    double y;
    
    char * name;
    
} TPLANE;


double distance ( TPLANE plane1, TPLANE plane2 );
void printClosestPlanesBruteForce ( TPLANE * planes, int count );
int readPlanes ( TPLANE ** data, int * cnt );

double minD ( double d1, double d2 ) {
    return d1 > d2 ? d2 : d1;
}

int main(void){
    
    TPLANE * planes;
    int cnt;
    
    printf("Zadejte lety:\n");
    
    if (!readPlanes(&planes, &cnt)){
        
        for (int i = 0; i < cnt; i++){
            free(planes[i].name);
        }
        
        free(planes);
        
        printf("Nespravny vstup.\n");
        return 1;
    }
    
    printClosestPlanesBruteForce ( planes, cnt );
    
    for (int i = 0; i < cnt; i++)
        free(planes[i].name);
       
    free(planes);
    return 0;
    
}

double distance ( TPLANE plane1, TPLANE plane2 ) {
    
    return sqrt(
                  ((plane1.x - plane2.x) * (plane1.x - plane2.x)) +
                  ((plane1.y - plane2.y) * (plane1.y - plane2.y))
                );
    
}

// nalezení nejbližších letadel O(n^2)
void printClosestPlanesBruteForce ( TPLANE * planes, int count ) {
    
    double minDistance = distance(planes[0], planes[1]);
    
    for (int i = 0; i < count; i++)
        for (int j = i + 1; j < count; j++)
            minDistance = minD(minDistance, distance(planes[i], planes[j]));
    
    printf("Nejmensi vzdalenost: %f\n", minDistance);
    
    for (int i = 0; i < count; i++)
        for (int j = i + 1; j < count; j++) {
            
            //Custom case pro 0
            if (minDistance < 0.0000001) {
                if (distance(planes[i], planes[j]) == minDistance) {
                    printf("%s - %s\n", planes[i].name, planes[j].name);
                }
            }
            else if ((distance(planes[i], planes[j]) - minDistance) < 0.0000001)
                printf("%s - %s\n", planes[i].name, planes[j].name);
                            
        }
    
}


// načítání pole viz. 8. proseminář START
int readPlanes ( TPLANE ** data, int * cnt) {
    
    TPLANE el, * tmp = (TPLANE *) malloc( 4 * sizeof( *tmp) );
    char c1, c2, c3, * elStr = NULL;
    int count = 0, max = 4, startPos = 0;
    double n1, n2;

    while (!feof(stdin)) {
        
        size_t strSize;
        int result = getline(&elStr, &strSize, stdin);
        
        if (result == EOF || strlen(elStr) <= 0 || elStr[0] == '\n') {
            fgetc(stdin);
            break;
        }
                
        if (sscanf(elStr, " %c %lf %c %lf %c %n", &c1, &n1, &c2, &n2, &c3, &startPos) < 5){
            
            //We're not using this anymore, we need to FREE it!
            free(elStr);
                     
            *data = tmp;
            *cnt = count;
            return 0;
            
        }
        
        if (c1 != '[' || c2 != ',' || c3 != ']') {
                     
            //We're not using this anymore, we need to FREE it!
            free(elStr);
            
            *data = tmp;
            *cnt = count;
            return 0;
            
        }
        
        el.name = (char *) malloc( strSize );
        
        if (count == max) {
            max += (max < 64 ? 8 : max / 2);
            tmp = (TPLANE *) realloc( tmp, max * sizeof( *tmp ) );
        }
        
        el.x = n1;
        el.y = n2;
        
        if (startPos > 0) {
            strncpy(el.name, elStr + startPos, strSize);
            el.name[strlen(el.name) - 1] = '\0';
        }
        else {
            el.name[0] = '\0';
        }
        
        tmp[count++] = el;
        
    }
    
    //We're not using this anymore, we need to FREE it!
    free(elStr);
    
    if (!feof(stdin) || count < 2) {
                 
        *data = tmp;
        *cnt = count;
        return 0;
        
    }
    
    *data = tmp;
    *cnt = count;
    return 1;
    
}
// načítání pole viz. 8 proseminář END
