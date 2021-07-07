#include <stdio.h>

/* Číslici vyskloňuje v závislosti na rodu */
#define MUZSKE 0
#define ZENSKE 1

/* Číslici vypíše jako kdyby byla samostatná (jedna, dvě, tři...) */
#define ZAKLADNI 2

/* Vypíše danou číslici slovně */
void vypisSlovne(int cislo, int rod){
  //Máme odsadit následující hodnotu / tisknout nulu?
  int odsadit = 0, tisknoutNulu = 1;

  //Nejdříve vytiskneme tisíce
  int tisicu = cislo / 1000;

  if (tisicu > 0) {
    //Výpis tisíců
    if (tisicu == 1) {
      printf("jeden tisíc");
    }
    else if (tisicu > 1 && tisicu < 5) {
      vypisSlovne(tisicu, 0); //vypíšeme slovně číslici tisíců
      printf(" tisíce");
    }
    else {
      vypisSlovne(tisicu, 0);
      printf(" tisíc");
    }

    //Tisíce máme vypsané, můžeme je odstranit
    cislo = cislo % 1000;
    odsadit = 1; //vypsali jsme tisíc, dále musíme odsazovat
    tisknoutNulu = 0; //už jsme něco vypsali, nemusíme tisknout nulu
  }

  //Stejný postup opakujeme pro stovky
  int stovek = cislo / 100;

  if (stovek > 0) {
    //Tentokrát ale musíme odsadit text, pokud už jsme vypisovali tisíce
    if (odsadit) {
      printf(" ");
      odsadit = 0;
    }

    //Výpis stovek
    if (stovek == 1)
      printf("jedno sto");
    else if (stovek == 2)
      printf("dvě stě"); //tady máme speciální skloňování
    else if (stovek > 2 && stovek < 5) {
      vypisSlovne(stovek, 1);
      printf(" sta");
    }
    else {
      vypisSlovne(stovek, 1);
      printf(" set");
    }

    //Opětovně nastavíme proměnné
    cislo = cislo % 100;
    odsadit = 1;
    tisknoutNulu = 0;
  }

  //U desítek stejný postup opakovat nemůžeme
  int desitek = cislo / 10;

  //Hodnoty 0-19 musíme řešit zvlášť
  if (desitek > 1) {
    //Nezapomínáme na odsazení
    if (odsadit) {
      printf(" ");
      odsadit = 0;
    }

    //Vypíšeme hodnotu určenou násobkem desítky
    switch (desitek) {
      case 9:
	printf("devadesát");
	break;
      case 8:
	printf("osmdesát");
	break;
      case 7:
	printf("sedmdesát");
	break;
      case 6:
	printf("šedesát");
	break;
      case 5:
	printf("padesát");
	break;
      case 4:
	printf("čtyřicet");
	break;
      case 3:
	printf("třicet");
	break;
      case 2:
	printf("dvacet");
	break;
    }

    //Opětovně nastavíme proměnné
    cislo = cislo % 10;
    odsadit = 1;
    tisknoutNulu = 0;
  }

  //Pokud máme co vypsat za odsazením, vypíšeme to
  if (odsadit && cislo != 0)
    printf(" ");

  //Nyní vypíšeme dané číslo
  switch (cislo){
    case 19:
      printf("devatenáct");
      break;
    case 18:
      printf("osmnáct");
      break;
    case 17:
      printf("sedmnáct");
      break;
    case 16:
      printf("šestnáct");
      break;
    case 15:
      printf("patnáct");
      break;
    case 14:
      printf("čtrnáct");
      break;
    case 13:
      printf("třináct");
      break;
    case 12:
      printf("dvanáct");
      break;
    case 11:
      printf("jedenáct");
      break;
    case 10:
      printf("deset");
      break;
    case 9:
      printf("devět");
      break;
    case 8:
      printf("osm");
      break;
    case 7:
      printf("sedm");
      break;
    case 6:
      printf("šest");
      break;
    case 5:
      printf("pět");
      break;
    case 4:
      printf("čtyři");
      break;
    case 3:
      printf("tři");
      break;
    case 2:
      if (rod == MUZSKE || rod == ZAKLADNI) //dva píšeme ze základu a u mužského rodu
        printf("dva");
      else
	printf("dvě");
      break;
    case 1:
      if (rod == ZENSKE || rod == ZAKLADNI) //jedna píšeme ze základu a u ženského rodu
        printf("jedna");
      else
	printf("jeden");
      break;
    case 0:
      if (tisknoutNulu) //pokud jsme nic nevypisovali a dostali jsme číslo 0, vypíšeme "nula"
	printf("nula");
      break;
  }
}

/* Hlavní funkce programu */
int main(void){

  int cislo;
  printf("Zadejte číslo k vypsání:\n");

  //Správný vstup je pouze číslo od 0 do 9999
  if (scanf("%d", &cislo) != 1 || cislo < 0 || cislo > 9999){
    printf("Nesprávný vstup.\n");
    return 1;
  }

  //Vypíšeme číslo slovně a odsadíme
  vypisSlovne(cislo, ZAKLADNI);
  printf("\n");

  return 0;
}

