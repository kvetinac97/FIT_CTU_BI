#include<stdio.h>
#include<stdlib.h>

typedef struct TCut {
    int width;
    int height;
    int cuts;
    
    struct TCut * next1;
    struct TCut * next2;
} TCUT;

int findBestCut ( int width, int height, int maxArea, TCUT ** next1, TCUT ** next2 );

TCUT * unique[50000000];
int showLine[2048] = {0};

void printIt ( TCUT * hr, int showPlus, int recursionDepth ) {
                
    if ( showPlus )
        showLine[recursionDepth] = 1;
    else
        showLine[recursionDepth] = 0;
            
    for ( int i = 1; i < recursionDepth; i++ )
        printf ( showLine[i] ? "| " : "  " );
    
    if ( showPlus && recursionDepth > 0 )
        printf ( "+-" );
    else if ( recursionDepth > 0 )
        printf ( "\\-" );
       
    printf ( "[%d,%d]\n", hr->width, hr->height );
        
    if ( hr->next1 )
        printIt ( hr->next1, 1, recursionDepth + 1 );
    
    if ( hr->next2 )
        printIt ( hr->next2, 0, recursionDepth + 1 );
        
}

int main ( void ) {
    
    int width = 0, height = 0, maxArea = -1;
    
    printf ( "Velikost:\n" );
    
    scanf ( "%d %d", &width, &height );
    
    if ( width <= 0 || height <= 0 ) {
        printf ( "Nespravny vstup.\n" );
        return 1;
    }
    
    printf ( "Maximalni plocha:\n" );
    
    scanf ( "%d", &maxArea );
    
    if ( maxArea <= 0 ) {
        printf ( "Nespravny vstup.\n" );
        return 1;
    }
    
    TCUT * first, * second;
    int cnt = findBestCut ( width, height, maxArea, &first, &second );
    printf ( "Rezu: %d\n", cnt );
    
    if ( cnt == 0 ) {
        printf ( "[%d,%d]\n", width, height );
        return 0;
    }
    
    TCUT * total = unique[width*10000 + height];
    printIt ( total, 0, 0 );
    
    return 0;
    
}

int findBestCut ( int width, int height, int maxArea, TCUT ** next1, TCUT ** next2 ) {
    
    *next1 = NULL;
    *next2 = NULL;
            
    if ( width * height <= maxArea && width <= 2 * height && height <= 2 * width ) // No need to cut
        return 0;
    
    int lowestCuts = 99999999;
    
    // Find options when dividing by width
    if ( width > height ) {
        
        for ( int width1 = ( width / 2 ); width1 > 0; width1-- ) {
                    
            if ( width1 <= 0 )
                continue;
            
            int width2 = width - width1;
            TCUT * cutsFirst = ( TCUT * ) malloc ( sizeof ( *cutsFirst ) ), * cutsSecond = ( TCUT * ) malloc ( sizeof ( *cutsSecond ) );
            
            if ( unique[width1*10000 + height] )
                cutsFirst = unique[width1*10000 + height];
            else {
                int num = findBestCut ( width1, height, maxArea, &cutsFirst->next1, &cutsFirst->next2 );
                
                cutsFirst->width = width1;
                cutsFirst->height = height;
                cutsFirst->cuts = num;
                unique[width1*10000 + height] = cutsFirst;
            }
                        
            if ( unique[width2*10000 + height] )
                cutsSecond = unique[width2*10000 + height];
            else {
                int num = findBestCut ( width2, height, maxArea, &cutsSecond->next1, &cutsSecond->next2 );
                
                cutsSecond->width = width2;
                cutsSecond->height = height;
                cutsSecond->cuts = num;
                unique[width2*10000 + height] = cutsSecond;
            }
            
            if ( cutsFirst->cuts < 0 || cutsSecond->cuts < 0 )
                continue;
                                    
            if ( ( cutsFirst->cuts + cutsSecond->cuts + 1 ) < lowestCuts ) {
                
                lowestCuts = cutsFirst->cuts + cutsSecond->cuts + 1;
                
                TCUT * cutsTotal = ( TCUT * ) malloc ( sizeof ( *cutsTotal ) );
                
                cutsTotal->width = width;
                cutsTotal->height = height;
                cutsTotal->cuts = lowestCuts;
                
                cutsTotal->next1 = cutsFirst;
                cutsTotal->next2 = cutsSecond;
                
                *next1 = cutsFirst;
                *next2 = cutsSecond;
                
                unique[width*10000 + height] = cutsTotal;
                
            }
            
        }
        
    }
    
    // Find options when dividing by height
    else {
        
        for ( int height1 = ( height / 2 ); height1 > 0; height1-- ) {
                    
            if ( height1 <= 0 )
                continue;
            
            int height2 = height - height1;
                                
            TCUT * cutsFirst = ( TCUT * ) malloc ( sizeof ( *cutsFirst ) ), * cutsSecond = ( TCUT * ) malloc ( sizeof ( *cutsSecond ) );

            if ( unique[width*10000 + height1] )
                cutsFirst = unique[width*10000 + height1];
            else {
                int num = findBestCut ( width, height1, maxArea, &cutsFirst->next1, &cutsFirst->next2 );
                
                cutsFirst->width = width;
                cutsFirst->height = height1;
                cutsFirst->cuts = num;
                unique[width*10000 + height1] = cutsFirst;
            }
                        
            if ( unique[width*10000 + height2] )
                cutsSecond = unique[width*10000 + height2];
            else {
                int num = findBestCut ( width, height2, maxArea, &cutsSecond->next1, &cutsSecond->next2 );
                
                cutsSecond->width = width;
                cutsSecond->height = height2;
                cutsSecond->cuts = num;
                unique[width*10000 + height2] = cutsSecond;
            }
            
            if ( cutsFirst->cuts < 0 || cutsSecond->cuts < 0 )
                continue;
                                    
            if ( ( cutsFirst->cuts + cutsSecond->cuts + 1 ) < lowestCuts ) {
                
                lowestCuts = cutsFirst->cuts + cutsSecond->cuts + 1;
                
                TCUT * cutsTotal = ( TCUT * ) malloc ( sizeof ( *cutsTotal ) );
                
                cutsTotal->width = width;
                cutsTotal->height = height;
                cutsTotal->cuts = lowestCuts;
                
                cutsTotal->next1 = cutsFirst;
                cutsTotal->next2 = cutsSecond;
                
                *next1 = cutsFirst;
                *next2 = cutsSecond;
                
                unique[width*10000 + height] = cutsTotal;
                
            }
            
        }
        
    }
        
    return lowestCuts;
    
}

