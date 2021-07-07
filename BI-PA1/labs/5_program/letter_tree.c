#include<stdio.h>
#include<math.h>
#include<float.h>

/*
 * Náhrada za zakázané modulo
 * princip: u sudého čísla je výsledek celočíselného dělení 2 stejný jako u neceločíselného
 * u lichého čísla bude tento výsledek přibližně 0.5
 */
int isEven(int n){
  double a = (double) (n / 2);
  double b = ((double) n) / 2;
  double eps = DBL_EPSILON * n * 64;

  return (fabs(a - b) < eps);
}

/*
 * Funkce pro nalezení minima 2 čísel
 * a tuto funkci využívající pro min. 4 čísel
 */
int min2(int a, int b){
  return a < b ? a : b;
}
int min4(int a, int b, int c, int d){
  return min2(a, min2(b, min2(c, d)));
}

/**
 * Hlavní funkce
 */
int main(void){
  //Počet zadaných n
  int n;

  if (scanf("%d", &n) != 1 || n < 1){
    printf("Nesprávný vstup.\n");
    return 1;
  }

  //Pro vypsání schématu n potřebujeme n/2 písmen abecedy, proto je maximum 51
  if (n > 51){
    printf("Příliš velký vstup.\n");
    return 1;
  }

  //Tvar pro sudá n zobrazíme vykreslením tvaru pro n + 1 s odebráním 1. a poslední řádky
  int constantIgnore = 0;

  if (isEven(n)){
    constantIgnore = 1;
    n += 1;
  }

  //První vypisované písmeno je to nejdále v abecedě (v ASCII tabulce nejvýše 90, nejméně 65)
  int max = 65 + n/2;

  //Písmeno na pozici [i, j] nalezneme jako minimum z i, j, (n-1)-i, (n-1)-j
  for (int i = constantIgnore; i < n - constantIgnore; i++){
    for (int j = 0; j < n; j++){
       printf("%c", max - min4(i, j, n-1-i, n-1-j));
    }
    printf("\n"); //odřádkování
  }

  return 0;
}
