#include<stdio.h>
#include<math.h>
#include<float.h>

int main(void){
  double a, b;

  printf("Zadejte dvě čísla: \n");
  if (scanf("%lf %lf", &a, &b) != 2){
    printf("Nespravny vstup.\n");
    return 1;
  }

  if (fabs(a - b) <= DBL_EPSILON * a * b * 64)
    printf("Cisla se rovnaji.\n");
  else
    printf("Cisla se nerovnaji.\n");

  return 0;
}
