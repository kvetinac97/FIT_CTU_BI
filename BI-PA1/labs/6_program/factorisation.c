#include<stdio.h>
#include<math.h>

void vypisRozklad(long long int num);

int main(void){
  long long int num;
  int result;
  printf("Zadavejte cisla: \n");

  while ((result = scanf("%lld", &num)) != EOF) {
    if (result != 1 || num <= 1) {
      printf("Neplatny vstup.\n");

      int c;
      do {
         c = fgetc(stdin);
      }
      while (c != '\n' && c != EOF);

      continue;
    }

    printf("%lld = ", num);
    vypisRozklad(num);
  }

  return 0;
}

/*
 * Funkce pro výpis rozkladu čísla n
 */
void vypisRozklad(long long int n){

  //Hledáme dělitele čísla n (tím, že jdeme od 2, budou vždy prvočíselné)
  for (long long int i = 2; i <= sqrt(n); i++){
    int mocnina = 0;
    long long int num = n;

    //Nalezneme nejvyšší mocninu prvočísla i v n
    while (num % i == 0) {
      mocnina++;
      num /= i;
    }

    //Pokud jsme v n nalezli alespoň 1. mocninu i, vypíšeme ji
    if (mocnina > 0){
      //Vypsání mocniny
      if (mocnina == 1) //1. varianta - zakomentovat toto
        printf("%lld", i); //1. varianta - zakomentovat toto
      else
        printf("%lld^%d", i, mocnina);

      //Pokud nám zbylo číslo větší než 1, opakujeme postup, jinak ukončíme
      if (num != 1) {
        printf(" * ");
        vypisRozklad(num);
      }
      else
        printf("\n");

      return;
    }
  }

  //Číslo je prvočíslem, můžeme ho vypsat
  //printf("%lld^1\n", n); //1. varianta
  printf("%lld\n", n); //2 .varianta
}

