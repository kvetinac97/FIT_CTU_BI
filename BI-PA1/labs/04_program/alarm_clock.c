#include<stdio.h>

int main(void){

  int hodinySpat, minutySpat, hodinyVstavat, minutyVstavat, casSpat, casVstavat;

  printf("Zadejte čas, kdy chcete jít spát:\n");

  if (scanf("%d:%d", &hodinySpat, &minutySpat) != 2 || hodinySpat < 0 || hodinySpat > 23 || minutySpat < 0 || minutySpat > 59){
    printf("Neplatný čas.");
    return 1;
  }

  printf("Zadejte čas, kdy chcete vstávat:\n");

  if (scanf("%d:%d", &hodinyVstavat, &minutyVstavat) != 2 || hodinyVstavat < 0 || hodinyVstavat > 23 || minutyVstavat < 0 || minutyVstavat > 59){
    printf("Neplatný čas.");
    return 1;
  }

  casSpat = hodinySpat * 60 + minutySpat;
  casVstavat = hodinyVstavat * 60 + minutyVstavat;

  if (casSpat > casVstavat)
    casVstavat += 24*60;

  int rozdil = casVstavat - casSpat;
  printf("Zbývá vám: %02d:%02d\n", rozdil / 60, rozdil % 60);

  return 0;
}

