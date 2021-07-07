#include<stdio.h>

void vypocet(int x, int y, int * soucet, int * rozdil, int * soucin, double * podil);

int main(void){
  int x, y, soucet, rozdil, soucin;
  double podil;

  printf("Zadejte dve cisla pro vypocet: \n");

  if (scanf("%d %d", &x, &y) != 2){
    printf("Nespravny vstup.\n");
    return 1;
  }

  vypocet(x, y, &soucet, &rozdil, &soucin, &podil);

  printf("%d + %d = %d\n%d - %d = %d\n%d * %d = %d\n%d / %d = %g\n", x, y, soucet, x, y, rozdil, x, y, soucin, x, y, podil);

  return 0;
}

void vypocet(int x, int y, int * soucet, int * rozdil, int * soucin, double * podil){
  *soucet = x + y;
  *rozdil = x - y;
  *soucin = x * y;
  *podil = (double) x / y;
}
