#include<stdio.h>

int main(void){

 int kodZbozi;  
 printf("Zadejte kód zboží:\n");

 if (scanf("%d", &kodZbozi) != 1 || kodZbozi < 1000 || kodZbozi > 9999){
   printf("Toto není správný kód zboží!");
   return 1;
 }

 int pocetKusu;  
 printf("Zadejte počet kusů:\n");

 if (scanf("%d", &pocetKusu) != 1 || pocetKusu < 1 || pocetKusu > 100){
   printf("Počet kusů musí být mezi 1 a 100 kusy!");
   return 1;
 }

 if (kodZbozi == 5908)
   printf("mléko\n%g Kč\n", pocetKusu * 16.5);

 else if (kodZbozi == 1274)
   printf("máslo\n%g Kč\n", pocetKusu * 48.2);

 else if (kodZbozi == 9832)
   printf("plyšák méďa\n%g Kč\n", pocetKusu * 123.4);

 else
   printf("zboží nenalezeno.");

  return 0;
}

