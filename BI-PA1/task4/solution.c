#include<stdio.h>
#include<stdlib.h>
#include<limits.h>

#define MAX_VESSEL_COUNT 200000

/*
 * ProgTest - 4th task
 * © Ondřej Wrzecionko 2019
 *
 * Algorithm:
 *
 *  - part 1: split vessels into simplified ones & sum up their volumes
 *   (example: vessel 0 3 1 1 can be split into 3 vessels {0, 1}, {1, 1}, {2, 1}
 *    vessel 1 3 2 2 then adds up so the result is {0, 1}, {1, 1+4}, {2, 1+4}, {3, 4} )
 *
 *  - part 2: fill up vessels from the lowest altitude to the highest one:
 *    if the total remaining volume equals volume of the simplified vessel,
 *    the vessel fills up and water ends up at altitude of the vessel + 1
 *
 *    if the total remaining volume is greater than volume of simplified vessel,
 *    we minimize the volume and continue up to the next vessel
 *
 *    if the total remaining volume is less than volume of simplified vessel,
 *    water ends up at vessels altitude + (remaining volume / simplified vessels volume)
 */

//Simplified vessel (height 1)
typedef struct TVessel {
    int alt;
    int volume;
    int totalVolume;
    int anchor;
} TVESSEL;

//Function headers
int loadVessels(TVESSEL * vessels, int n, int * posUsed,
                 int * lowestIndex, int * highestIndex);
void printVesselState(TVESSEL * vessels, int * posUsed, int volume,
                int * lowestIndex, int * highestIndex);

int main(void) {

    /*
     * vessels: holds all simplified vessels
     * posUsed: keeps track of which positions are already used (0 = unused, 1 = used)
     *
     * n: number of vessels we need to read from input
     * totalVolume: volume of all vessels for easier overflow check
     * lowestIndex, highestIndex: help us to iterate through data
     */

    TVESSEL vessels[2 * MAX_VESSEL_COUNT];
    int posUsed[2 * MAX_VESSEL_COUNT] = {0};

    int n, totalVolume = 0, lowestIndex = INT_MAX, highestIndex = INT_MIN;

    printf("Zadejte pocet nadrzi:\n");

    //Load & validate vessel count
    if (scanf("%d", &n) != 1 || n <= 0 || n > MAX_VESSEL_COUNT){
        printf("Nespravny vstup.\n");
        return 1;
    }

    //Load each vessel
    if (!loadVessels(vessels, n, posUsed, &lowestIndex, &highestIndex)){
        printf("Nespravny vstup.\n");
        return 1;
    }
    
    int lastNotNull = -MAX_VESSEL_COUNT;
    for (int pos = lowestIndex; pos <= highestIndex; pos++){

        //Ignore vessels at altitudes that were not set
        if (!posUsed[pos]) {
            vessels[pos].totalVolume = totalVolume;
            vessels[pos].anchor = lastNotNull;

            continue;
        }

        totalVolume += vessels[pos].volume;
        vessels[pos].totalVolume = totalVolume;
        vessels[pos].anchor = pos;
        lastNotNull = pos;
        
    }
    
    printf("Zadejte objem vody:\n");

    do {
        
        //Read volume from input
        int volume, scanResult;
        
        scanResult = scanf("%d", &volume);

        //Quit cycle if we reached end of file
        if (scanResult == EOF)
            break;

        //Validate scan result and volume
        if (scanResult != 1 || volume < 0){
            printf("Nespravny vstup.\n");
            return 1;
        }

        //Empty volume
        if (volume == 0) {
            printf("Prazdne.\n");
            continue;
        }
        
        //Overflow
        if (volume > totalVolume) {
            printf("Pretece.\n");
            continue;
        }
        
        int low = lowestIndex, high = highestIndex+1, loops = 0;
        
        while (loops++ < 10000000){
            
            int poz = (low+high)/2;
            
            if (vessels[poz].totalVolume == volume){
                printf("h = %f\n", (double) (vessels[poz].anchor + 1 - MAX_VESSEL_COUNT));
                break;
            }
            
            if (vessels[poz].totalVolume < volume){
                
                if (vessels[poz+1].totalVolume > volume) {
                    
                    printf("h = %f\n", (volume - vessels[poz].totalVolume) / (double) (vessels[poz+1].totalVolume - vessels[poz].totalVolume) + (poz+1 - MAX_VESSEL_COUNT));
                    
                    break;
                }
                else {
                    low = poz;
                }
                
            }
            
            if (vessels[poz].totalVolume > volume) {
                
                if (vessels[poz-1].totalVolume < volume) {
                    
                    printf("h = %f\n", (volume - vessels[poz-1].totalVolume) / (double) (vessels[poz].totalVolume - vessels[poz-1].totalVolume) + (poz - MAX_VESSEL_COUNT));
                    
                    break;
                }
                else {
                    high = poz;
                }
                
            }
            
        }

    }
    while (1);

    //Successfull end
    return 0;
}

//Funcion to load vessels and convert them to simplified ones
int loadVessels(TVESSEL * vessels, int n, int * posUsed,
                 int * lowestIndex, int * highestIndex) {

    printf("Zadejte parametry nadrzi:\n");

    //We know the vessel count, so we can iterate and load each of them
    for (int i = 0; i < n; i++){

        /*
         * alt: altitude of vessel
         * height, width, depth: properties of vessel
         * crossSection: S = w * d
         */
        int alt, height, width, depth, crossSection;
             
        //Load and validate input
        if (scanf("%d %d %d %d", &alt, &height, &width, &depth) != 4 ||
            height <= 0 || width <= 0 || depth <= 0) {

            return 0;

        }

        //Set cross section and keep track of total volume for overflow control
        crossSection = width * depth;

        //Split vessel into simplified ones
        for (int j = alt; j < alt + height; j++){

            //Add MAX_VESSEL_COUNT so we can hold negative values
            int index = MAX_VESSEL_COUNT + j;

            //No simplified vessel with altitude index exists
            if (!posUsed[index]) {

                //Control highestIndex and lowestIndex
                if (index > *highestIndex)
                    *highestIndex = index;

                if (index < *lowestIndex)
                    *lowestIndex = index;

                //The simplified vessel now exists
                posUsed[index] = 1;

                //Create the simplified vessel
                vessels[index].alt = index;
                vessels[index].volume = crossSection;
       
            }
            else //Just add the cross section to the volume
                vessels[index].volume += crossSection;

        }

    }

    return 1;

}
