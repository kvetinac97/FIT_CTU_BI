#include<stdio.h>
#include<string.h>

#define TEXT_MAX 128

//Předám si odkaz na první znak řetězce
void strtoupper(char * retezec) {

  //Dokud se nejedná o nulový znak (konec řetězce)
  while (*retezec != '\0') {

    //Pokud je to znak malého písmena, převedu ho na velké
    if (*retezec >= 97 && *retezec <= 122)
      *retezec = (*retezec - 32);

    //Posunu se v řetězci
    retezec++;

  }

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

  char retezec[TEXT_MAX];
  printf("Zadejte textovy retezec:\n");

  nactiRetezec(retezec);

  //Samotné převedení a vypsání převedeného řetězce
  strtoupper(retezec);
  printf("%s\n", retezec);

  return 0;
}

