#include<stdio.h>

//a1x + b1y + c1 = 0
//a2x + b2y + c2 = 0

//a1x + b1y = -c1
//a2x + b2y = -c2

//Determinant A:
//a1 b1
//a2 b2
// = a1 * b2 - a2 * b1

//Determinant Ax
//-c1 b1
//-c2 b2
// = c2 * b1 - b2 * c1

//Determinant Ay
//a1 -c1
//a2 -c2
// = a2 * c1 - a1 * c2

//x = Ax/A
//y = Ay/A
int vypoctiSoustavuRovnic(int a1, int b1, int c1, int a2, int b2, int c2, double * x, double * y){

  int detA = (a1 * b2) - (a2 * b1);
  if (detA == 0)
    return 0;

  int detAx = (c2 * b1) - (b2 * c1);
  int detAy = (a2 * c1) - (a1 * c2);

  *x = detAx / (double) detA;
  *y = detAy / (double) detA;

  return 1;
}

int main(void){

  double x, y;
  int a1, b1, c1, a2, b2, c2;

  printf("Zadejte cisla a1, b1, c1, a2, b2, c2: \n");

  if (scanf("%d %d %d %d %d %d", &a1, &b1, &c1, &a2, &b2, &c2) != 6){
    printf("Nespravny vstup.\n");
    return 1;
  }

  if (!vypoctiSoustavuRovnic(a1, b1, c1, a2, b2, c2, &x, &y)){
    printf("Rovnice nema reseni nebo jsou jejim reseni vsechna realna cisla.\n");
    return 1;
  }

  printf("X = %g, Y = %g\n", x, y);
  return 0;
}
