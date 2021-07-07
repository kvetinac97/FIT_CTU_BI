#include<stdio.h>

int main(void){

  int n;
  printf("Zadejte číslo, do kterého chcete sudá čísla:\n");

  if (scanf("%d", &n) != 1){
    printf("Nesprávný vstup.\n");
    return 1;
  }

  for (int i = 0; i <= n; i++){
    if (i % 2 == 0)
      printf("%d ", i);
  }

  printf("\n");
  return 0;
}

