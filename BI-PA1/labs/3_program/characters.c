#include <stdio.h>

int main(void){
  char pismeno;
  printf("Zadej písmeno: ");

  if (scanf("%c", &pismeno) != 1){
    printf("Chyba - neplatný znak.\n");
    return 1;
  }

  int ascii = (int) pismeno;
  if (ascii < 65 || (ascii > 90 && ascii < 97) || ascii > 122){
    printf("Chyba - toto není písmeno.\n");
    return 1;
  }

  char posunute;

  if (pismeno == 'z')
    posunute = 'a';
  else if (pismeno == 'Z')
    posunute = 'A';
  else
    posunute = (char) (ascii + 1);

  printf("Posunuté písmeno: %c\n", posunute);
  return 0;
}
