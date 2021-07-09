#include<stdio.h>
#include<time.h>
#include<stdlib.h>

int main(void){

  time_t t;
  srand((unsigned) time(&t));

  int pseudoCislo = rand() % 101;
  int read, uhodnuti = 0;

  printf("Hadej cislo: <0;100>\n");

  while (uhodnuti != EOF){
    if ((uhodnuti = scanf("%d", &read)) == 0){
      printf("Toto neni cislo :/\n");

      int uh;
      while (1) {
         uh = fgetc(stdin);
         if (uh == '\n' || uh == EOF)
            break;
      }

      continue;
    }

    if (read == pseudoCislo){
      printf("Vyhral jsi, frajere!\n");
      return 0;
    }

    if (read < pseudoCislo){
       printf("Zkus vetsi cislo.\n");
    }
    else {
       printf("Zkus mensi cislo.\n");
    }
  }

  printf("Skoda, ze jsi to vzdal :(\n");
  return 0;
}
