#include<stdio.h>
#include<stdlib.h>

int memCounter = 0;

int neplatneRadky ( int * pole, int radek, int sloupcu );
int neplatneSloupce ( int * pole, int radek, int sloupcu );
int spocitejMozneHodnoty ( int * pole, int radek, int sloupcu, int prvniPrazdnaPozice, int mozneHodnoty[9] );
int vyres ( int * pole, int radek, int sloupcu, int tisknout );
void vytiskniReseni ( int * pole, int radek, int sloupcu );
int nactiPole ( int ** pole, int * radekP, int * sloupcuP );
int kontrolaPole ( int * pole, int radek, int sloupcu );

int main ( void ) {
    
    int * pole = NULL;
    int radek = 0, sloupcu = 0;
    
    printf("Zadejte kakuro:\n");
    
    if ( ! nactiPole ( &pole, &radek, &sloupcu ) ) {
        printf("Nespravny vstup.\n");
        return 1;
    }
    
    if ( ! kontrolaPole ( pole, radek, sloupcu ) ) {
        printf("Nespravny vstup.\n");
        free ( pole );
        return 1;
    }

    int reseni = vyres ( pole, radek, sloupcu, 0 );
        
    if ( reseni == 0 ) {
        printf("Reseni neexistuje.\n");
        free ( pole );
        return 0;
    }
    
    if ( reseni == 1 ) {
        
        printf("Kakuro ma jedno reseni:");
        vyres ( pole, radek, sloupcu, 1 );
        printf("\n");
                
        free ( pole );
        return 0;
        
    }
    
    printf("Celkem ruznych reseni: %d\n", reseni);
        
    free ( pole );
    return 0;
    
}

int neplatneRadky ( int * pole, int radek, int sloupcu ) {
    
   for ( int i = 0; i < radek; i++ ) {
        
       int frequencyTable[10] = {0};
       int predpokladanySoucet = 0, soucet = 0;
        
        for ( int j = 0; j < sloupcu; j++ ) {
            
            int value = pole [ i * sloupcu + j ];
            
            if ( value >= 1000 ) {
                
                if ( !frequencyTable[0] && soucet != predpokladanySoucet && soucet != 0 && predpokladanySoucet != 0 )
                    return 1;
                
                predpokladanySoucet = ( value / 1000 );
                for (int xx = 0; xx < 10; xx++)
                    frequencyTable[xx] = 0;
                soucet = 0;
            }
            
            else if ( value < 10 && value >= 0 ) {
                
                if ( value != 0 && frequencyTable[value] )
                    return 1;
                
                frequencyTable[value] = 1;
                soucet += value;
            }
            
            else if ( value == -1 ){
                
                if ( !frequencyTable[0] && soucet != predpokladanySoucet && soucet != 0 && predpokladanySoucet != 0 )
                    return 1;
                                
                if ( soucet > predpokladanySoucet )
                    return 1;
                
                predpokladanySoucet = 0;
                for (int xx = 0; xx < 10; xx++)
                    frequencyTable[xx] = 0;
                soucet = 0;
                
            }
            
        }
        
        if ( !frequencyTable[0] && soucet != predpokladanySoucet && soucet != 0 && predpokladanySoucet != 0 )
            return 1;
                        
        if ( soucet > predpokladanySoucet && predpokladanySoucet != 0 )
            return 1;
        
    }
    
    return 0;
    
}

int neplatneSloupce ( int * pole, int radek, int sloupcu ) {
    
    for ( int i = 0; i < sloupcu; i++ ) {
        
        int frequencyTable[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int predpokladanySoucet = 0, soucet = 0;
        
        for ( int j = 0; j < radek; j++ ) {
            
            int value = pole[j * sloupcu + i];
            
            if ( ( value % 1000 ) >= 10 ) {
                
                if ( !frequencyTable[0] && soucet != predpokladanySoucet && soucet != 0 && predpokladanySoucet != 0 )
                    return 1;
                
                predpokladanySoucet = ( ( value % 1000 ) / 10 );
                for (int xx = 0; xx < 10; xx++)
                    frequencyTable[xx] = 0;
                soucet = 0;
            }
            
            else if ( value < 10 && value >= 0 ) {
                
                if ( value != 0 && frequencyTable[value] )
                    return 1;
                
                frequencyTable[value] = 1;
                soucet += value;
            }
            
            else if ( value == -1 ){
                
                if ( !frequencyTable[0] && soucet != predpokladanySoucet && soucet != 0 && predpokladanySoucet != 0 )
                    return 1;
                                
                if ( soucet > predpokladanySoucet && predpokladanySoucet != 0 )
                    return 1;
                
                predpokladanySoucet = 0;
                for (int xx = 0; xx < 10; xx++)
                    frequencyTable[xx] = 0;
                soucet = 0;
                
            }
            
        }
        
        if ( !frequencyTable[0] && soucet != predpokladanySoucet && soucet != 0 && predpokladanySoucet != 0 )
            return 1;
                        
        if ( soucet > predpokladanySoucet && predpokladanySoucet != 0 )
            return 1;
        
    }
    
    return 0;
    
}

int spocitejMozneHodnoty ( int * pole, int radek, int sloupcu, int prvniPrazdnaPozice, int mozneHodnoty[9] ) {
    
    memCounter++;
    
    if (memCounter > 3250000)
        return 0;
    
    int cisloRadku = prvniPrazdnaPozice / sloupcu;
    int cisloSloupce = prvniPrazdnaPozice % sloupcu;
    
    if ( cisloRadku * sloupcu + cisloSloupce > radek * sloupcu )
        return 0;
    
    int foundValue = 0;
    int frequencyTable[10] = {0};
    int predpokladanySoucet = 0, soucet = 0, zbyvaNul = 0;
    
    for ( int i = 0; i < sloupcu; i++ ) {
        
        if ( i == cisloSloupce )
            foundValue = 1;
        
        int value = pole[cisloRadku * sloupcu + i];
        
        if ( value >= 1000 ) {
            
            if (foundValue)
                break;
            
            predpokladanySoucet = ( value / 1000 );
            for (int xx = 0; xx < 10; xx++)
                frequencyTable[xx] = 0;
            soucet = 0;
            zbyvaNul = 0;
        }
        
        else if ( value < 10 && value >= 0 ) {
            
            if (value == 0)
                zbyvaNul++;
            
            frequencyTable[value] = 1;
            soucet += value;
        }
        
        else if ( value == -1 ) {
            
            if (foundValue)
                break;
            
            predpokladanySoucet = 0;
            for (int xx = 0; xx < 10; xx++)
                frequencyTable[xx] = 0;
            soucet = 0;
            zbyvaNul = 0;
            
        }
        
    }
    
    int zbyvaRadek = predpokladanySoucet - soucet;
    
    if ( zbyvaRadek <= 0 )
        return 0;
    
    if ( zbyvaNul == 1 ) {
        
        if (zbyvaRadek > 9 || zbyvaRadek < 1)
            return 0;
        
        mozneHodnoty[0] = zbyvaRadek;
        return 1;
    }
    
    int min1 = 1, min2 = 1, max1 = 9, max2 = 9;
     
    if ( zbyvaNul == 2 ) {
        
        min1 = 1;
        
        while ( ( zbyvaRadek - min1 ) > 9 )
            min1++;
        
        max1 = zbyvaRadek - min1;
        
    }
        
    int zbyvaNul2 = 0, frequencyTable2[10] = {0};
    predpokladanySoucet = 0;
    soucet = 0;
    foundValue = 0;
    
    for ( int i = 0; i < radek; i++ ) {
        
        if ( i == cisloRadku )
            foundValue = 1;
        
        int value = pole[i * sloupcu + cisloSloupce];
        
        if ( ( value % 1000 ) >= 10 ) {
            
            if (foundValue)
                break;
            
            predpokladanySoucet = ( ( value % 1000 ) / 10 );
            for (int xx = 0; xx < 10; xx++)
                frequencyTable2[xx] = 0;
            soucet = 0;
            zbyvaNul2 = 0;
        }
        
        else if ( value < 10 && value >= 0 ) {
            if ( value == 0 )
                zbyvaNul2++;
            
            frequencyTable2[value] = 1;
            soucet += value;
        }
        
        else if ( value == -1 ) {
            
            if (foundValue)
                break;
            
            predpokladanySoucet = 0;
            for (int xx = 0; xx < 10; xx++)
                frequencyTable2[xx] = 0;
            soucet = 0;
            zbyvaNul2 = 0;
            
        }
        
    }
    
    int zbyvaSloupec = predpokladanySoucet - soucet;
        
    if ( zbyvaSloupec <= 0 )
        return 0;
    
    if ( zbyvaNul2 == 1 ) {
        
        if (zbyvaSloupec > 9 || zbyvaSloupec < 1)
            return 0;
        
        mozneHodnoty[0] = zbyvaSloupec;
        return 1;
    }
    
    if ( zbyvaNul2 == 2 ) {
        
        min2 = 1;
        
        while ( ( zbyvaSloupec - min2 ) > 9 )
            min2++;
        
        max2 = zbyvaSloupec - min2;
        
    }

    
    int a = 0;
    
    for ( int i = ( min1 < min2 ? min1 : min2 ); i <= ( max1 < max2 ? max1 : max2 ); i++ ) {
        
        if ( i < 0 || i > 9 || frequencyTable[i] || frequencyTable2[i] )
            continue;
        
        mozneHodnoty[a++] = i;
        
    }
    
    //printf("Moznych %d hodnot pro %d, %d\n", a, zbyvaRadek, zbyvaSloupec);
    return a;
    
}

int vyres ( int * pole, int radek, int sloupcu, int tisknout ) {
    
    if ( neplatneRadky ( pole, radek, sloupcu ) || neplatneSloupce ( pole, radek, sloupcu ) )
        return 0;
    
    int prvniPrazdnaPozice;
    
    int realI = -1, nejmene = 10;
    int policko[10];
    
    for ( int i = 0; i < radek * sloupcu; i++ )
        if ( pole[i] == 0 ) {
            
            int x = spocitejMozneHodnoty ( pole, radek, sloupcu, i, policko );
            
            if ( x < nejmene ) {
                realI = i;
                nejmene = x;
            }
            
        }
    
    if ( realI != -1 )
        prvniPrazdnaPozice = realI;
    else {
        
        if (tisknout)
            vytiskniReseni ( pole, radek, sloupcu );
        
        return 1;
        
    }
    
    int vyreseno = 0, mozneHodnoty[9] = {0};
    int moznych = spocitejMozneHodnoty ( pole, radek, sloupcu, prvniPrazdnaPozice, mozneHodnoty );
    
    if ( moznych <= 0 || prvniPrazdnaPozice < 0 )
        return 0;
    
    for ( int i = 0; i < moznych; i++ ) {
        if (i > 9 || i < 0)
            continue;
        
        pole[prvniPrazdnaPozice] = mozneHodnoty[i];
        int num = vyres ( pole, radek, sloupcu, tisknout );
        vyreseno += num;
    }
    
    pole[prvniPrazdnaPozice] = 0;
    return vyreseno;
}

void vytiskniReseni ( int * pole, int radek, int sloupcu ) {
    
    for ( int i = 0; i < radek * sloupcu; i++ ) {
        
        int value = pole[i];
        
        if ( i % sloupcu == 0 )
            printf("\n");
        
        if ( i % sloupcu == ( sloupcu - 1 ) ) {
                        
            if ( value == -1 )
                printf("X");
            
            if (value == 0)
                printf(".");
            
            if ( value > 0 && value < 10 )
                printf("%d", value);
            
            if ( value >= 10 && value < 1000 )
                printf("%d\\X", value / 10);
            
            if ( value >= 1000 && ( ( value % 1000 ) < 10 ) )
                printf("X\\%d", value / 1000);
            
            if ( value >= 1000 && ( ( value % 1000 ) >= 10 ) )
                printf("%d\\%d", ( value % 1000 ) / 10, value / 1000);
            
        }
        
        else {
            
            if ( value == -1 )
               printf("X     ");
           
           if (value == 0)
               printf(".     ");
           
           if ( value > 0 && value < 10 )
               printf("%d     ", value);
           
           if ( value >= 10 && value < 1000 )
               printf("%d\\X%s  ", value / 10, ( value / 10 ) > 9 ? "" : " ");
           
           if ( value >= 1000 && ( ( value % 1000 ) < 10 ) )
               printf("X\\%d%s  ", value / 1000, ( value / 1000 ) > 9 ? "" : " ");
           
           if ( value >= 1000 && ( ( value % 1000 ) >= 10 ) )
               printf("%d\\%d%s%s ", ( value % 1000 ) / 10, value / 1000,
                      ( value / 1000 ) > 9 ? "" : " ", ( ( value % 1000 ) / 10 ) > 9 ? "" : " ");
            
        }
    
        
    }
        
}

int kontrolaPole ( int * pole, int radek, int sloupcu ) {
    
    for ( int i = 0; i < radek; i++ ) {
        
        int scitaciPole = 0, prazdnychPoli = 0;
        
        for ( int j = 0; j < sloupcu; j++ ) {
            
            if ( pole[i * sloupcu + j] >= 1000 ) {
                scitaciPole = 1;
                prazdnychPoli = 0;
            }
            else if ( pole[i * sloupcu + j] == 0 )
                prazdnychPoli++;
            else if ( pole[i * sloupcu + j] == -1 ){
                
                if ( ( scitaciPole && prazdnychPoli == 0 ) || ( !scitaciPole && prazdnychPoli > 0 ) || prazdnychPoli > 9 )
                    return 0;
                
                scitaciPole = 0;
                prazdnychPoli = 0;
            }
            
        }
        
        if ( ( scitaciPole && prazdnychPoli == 0 ) || ( !scitaciPole && prazdnychPoli > 0 ) || prazdnychPoli > 9 )
            return 0;
        
    }
    
    for ( int i = 0; i < sloupcu; i++ ) {
        
        int scitaciPole = 0, prazdnychPoli = 0;
        
        for ( int j = 0; j < radek; j++ ) {
            
            if ( ( pole[j * sloupcu + i] % 1000 ) >= 10 ) {
                scitaciPole = 1;
                prazdnychPoli = 0;
            }
            else if ( pole[j * sloupcu + i] == 0 )
                prazdnychPoli++;
            else if ( pole[j * sloupcu + i] == -1 ){
                
                if ( ( scitaciPole && prazdnychPoli == 0 ) || ( !scitaciPole && prazdnychPoli > 0 ) || prazdnychPoli > 9 )
                    return 0;
                
                scitaciPole = 0;
                prazdnychPoli = 0;
            }
            
        }
        
        if ( ( scitaciPole && prazdnychPoli == 0 ) || ( !scitaciPole && prazdnychPoli > 0 ) || prazdnychPoli > 9 )
            return 0;
        
    }
    
    return 1;
    
}

int nactiPole ( int ** pole, int * radekP, int * sloupcuP ) {
    
    int sloupcu = 0, radek = 0, * docasne = ( int * ) malloc ( 4096 * sizeof ( *docasne ) );
    char * radka, * originalniRadka = NULL;
    size_t velikostRadky;
    
    while ( getline( &originalniRadka, &velikostRadky, stdin ) != EOF ) {
        
        radka = originalniRadka;
        int cislo, dolu, doprava, zbylo, sloupec = 0;
        char oddelovac1, oddelovac2;
        
        if ( radek % 3 == 0 && radek != 0 )
            docasne = ( int * ) realloc ( docasne, ( 3 + radek ) * sloupcu * sizeof ( *docasne ) );
        
        while ( *radka ) {
            
            if ( sscanf ( radka, " %d%c%d %n", &dolu, &oddelovac1, &doprava, &zbylo ) == 3 ) {
                
                if ( oddelovac1 != '\\' || dolu < 1 || dolu > 45 || doprava < 1 || doprava > 45 ) {
                    
                    free ( originalniRadka );
                    free ( docasne );
                    return 0;
                    
                }
                
                cislo = ( dolu * 10 ) + ( doprava * 1000 );
                sloupec++;
                radka += zbylo;
                
            }
            
            else if ( sscanf ( radka, " %d%c%c %n", &dolu, &oddelovac1, &oddelovac2, &zbylo ) == 3
                      && oddelovac1 == '\\' && oddelovac2 == 'X' ) {
                
                if ( dolu < 1 || dolu > 45 ) {
                    
                    free ( originalniRadka );
                    free ( docasne );
                    return 0;
                    
                }
                
                cislo = ( dolu * 10 );
                sloupec++;
                radka += zbylo;
                
            }
            
            else if ( sscanf ( radka, " %c%c%d %n", &oddelovac1, &oddelovac2, &doprava, &zbylo ) == 3
                     && oddelovac1 == 'X' && oddelovac2 == '\\' ) {
                
                if ( doprava < 1 || doprava > 45 ) {
                    
                    free ( originalniRadka );
                    free ( docasne );
                    return 0;
                    
                }
                
                cislo = ( doprava * 1000 );
                sloupec++;
                radka += zbylo;
                
            }
            
            else if ( sscanf ( radka, " %c %n", &oddelovac1, &zbylo ) == 1 ) {
                
                if ( oddelovac1 != '.' && oddelovac1 != 'X' ) {
                    
                    free ( originalniRadka );
                    free ( docasne );
                    return 0;
                    
                }
                
                cislo = ( oddelovac1 == '.' ? 0 : -1 );
                sloupec++;
                radka += zbylo;
                
            }
            
            else {
                
                free ( originalniRadka );
                free ( docasne );
                return 0;
                
            }
            
            if ( ( sloupec > sloupcu && radek > 0 ) || sloupcu > 32 ) {
                
                free ( originalniRadka );
                free ( docasne );
                return 0;
                
            }
            
            docasne [ ( radek * sloupcu ) + ( sloupec - 1 ) ] = cislo;
            
        }
        
        if ( radek == 0 )
            sloupcu = sloupec;
            
        else if ( sloupec != sloupcu || radek > 32 ) {
            
            free ( originalniRadka );
            free ( docasne );
            return 0;
            
        }
        
        radek++;
        
    }
    
    free ( originalniRadka );
    
    *pole = docasne;
    *radekP = radek;
    *sloupcuP = sloupcu;
    return 1;
    
}
