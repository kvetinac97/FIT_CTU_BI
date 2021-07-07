#include <stdio.h>

int main(void){
  unsigned int x;
  printf("Zadejte celé kladné číslo x: ");
  scanf("%u", &x);

  unsigned int chybejici = (1 + 2 + 3 + 4);
  unsigned int y;

  //for (int poz = 1; poz < 5; poz++){
    printf("Zadejte 1. číslo: ");
    scanf("%u", &y);
    chybejici += (x - y);

    printf("Zadejte 2. číslo: ");
    scanf("%u", &y);
    chybejici += (x - y);

    printf("Zadejte 3. číslo: ");
    scanf("%u", &y);
    chybejici += (x - y);

    printf("Zadejte 4. číslo: ");
    scanf("%u", &y);
    chybejici += (x - y);
  //}

  printf("Chybí číslo %u\n", x + chybejici);

  return 0;
}
