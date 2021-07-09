#include<stdio.h>
#include<string.h>

#define TEXT_MAX 128

//Hashovací funkce djb2 (http://www.cse.yorku.ca/~oz/hash.html)
long hash(char *str) {

  long hash = 5381;
  int c;

  while ((c = *str++))
    hash = ((hash << 5) + hash) + c; /* hash * 33 + c */

  return hash;

}

void nactiRetezec(char* retezec){

  fgets(retezec, TEXT_MAX, stdin);
  int delka = strlen(retezec);

  if (retezec[delka - 1] == '\n')
    retezec[delka - 1] = '\0';
  else
    printf("\n");

}

int main(void){

  char retezec1[TEXT_MAX];
  char retezec2[TEXT_MAX];

  printf("Zadejte textovy retezec 1:\n");
  nactiRetezec(retezec1);

  printf("Zadejte textovy retezec 2:\n");
  nactiRetezec(retezec2);

  long hash1 = hash(retezec1);
  long hash2 = hash(retezec2);

  printf((hash1 == hash2) ? "Otisky retezcu jsou stejne.\n" : "Otisky retezcu se lisi.\n");

  return 0;
}

