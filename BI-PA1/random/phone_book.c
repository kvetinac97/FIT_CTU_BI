#include<stdio.h>
#include<ctype.h>
#include<string.h>

#define MAX_CHAR_COUNT 203
#define MAX_CONTACT_COUNT 100

/*
 * Program, který funguje jako telefonní číselník
 * na standardní vstup vyžaduje seznam kontaktů ve formátu:
 *
 * Jméno Příjmení
 * 123456789
 *
 * Následně mu jsou jako argumenty předány jendotlivé filtry k hledání
 * (fungují stylem 6 může být mno nebo číslice 6, 2 abc ... )
 *
 * Program vypíše nalezené telefonní kontakty ze seznamu
 */
int nactiKontaktyZeVstupu(char kontakty[MAX_CONTACT_COUNT][MAX_CHAR_COUNT], int * pocetKontaktu) {
    char c;
    int i = 0, zapisTelefonu = 0;
    
    while (!feof(stdin)) {
        
        c = fgetc(stdin);

        if (c == '\n' && zapisTelefonu){ //ukončení zápisu
            kontakty[*pocetKontaktu][i++] = '\0';
            
            //Reset
            (*pocetKontaktu)++;
            zapisTelefonu = 0;
            i = 0;
            continue;
        }
        else if (c == '\n'){
            zapisTelefonu = 1; //přechod z jména na telefon

            kontakty[*pocetKontaktu][i++] = ',';
            kontakty[*pocetKontaktu][i++] = ' ';
            continue;
        }
        
        //Chyba: příliš dlouhé jméno kontaktu
        if (!zapisTelefonu && i >= 100){
            printf("Chyba: prilis dlouhe jmeno kontaktu.\n");
            return 0;
        }
        
        //Chyba: telefon nesmí být delší než 12 číslic a +
        if (zapisTelefonu && (i >= 114 || ((c < 48 || c > 57) && c != '+'))){
            printf("Chyba: prilis dlouhe / nejedna se o telefonni cislo.\n");
            return 0;
        }
        
        kontakty[*pocetKontaktu][i++] = c;
    }
    
    //Nedokončili jsme zápis
    if (zapisTelefonu || i > 1){
        printf("Chyba: na vstupu byly jeste nejake znaky navic\n");
        return 0;
    }
    
    return 1;
}

void vytiskniVse(char kontakty[MAX_CONTACT_COUNT][MAX_CHAR_COUNT], int pocet) {
    //Vytisteni kontaktu
    for (int i = 0; i < pocet; i++){
        printf("%s\n", kontakty[i]);
    }
}

char nahradaZnaku(char c){
    //Náhrada znaků za číslici
    char clower = tolower(c);
    
    //Pro a-o je vzorec pro číslo lehce vypočítatelný
    if (clower >= 97 && clower < 112){
        return (char) (48 + (((clower-97) / 3) + 2));
    }
    //p-s = 7
    if (clower >= 112 && clower <= 115)
        return (char) 55;
    //t-v = 8
    if (clower >= 116 && clower <= 118)
        return (char) 56;
    //w-z = 9
    if (clower >= 119 && clower <= 122)
        return (char) 57;
    
    if (clower == 43)
        return (char) 48;
    
    return clower;
}

int splnujeFiltr(char * retezec, char * filtr){
    //Zkusíme najít podřetězec délky filtru, který bude shodný s filtrem
    int delkaFiltru = strlen(filtr);
    if (delkaFiltru > 100) //příliš dlouhé, nelze splnit
        return 0;
    
    char podretezec[101];
    
    for (int i = 0; i < strlen(retezec) - strlen(filtr) + 1; i++){
        //Vytvoření podřetězce
        for (int j = 0; j < delkaFiltru; j++)
            podretezec[j] = nahradaZnaku(retezec[i + j]);
        
        //Porovnání
        if (strncmp(podretezec, filtr, delkaFiltru) == 0)
            return 1;
    }
    
    return 0;
}

int vyfiltrujKontakty(char kontakty[MAX_CONTACT_COUNT][MAX_CHAR_COUNT], int pocetKontaktu, char * filtr, int * pasujiciKontakty) {
    //Filtr
    int found = 0;
    
    for (int i = 0; i < pocetKontaktu; i++){
        
        if (splnujeFiltr(kontakty[i], filtr)){
            found = 1;
            pasujiciKontakty[i] = 1;
        }
        
    }
    
    return found;
}

void vytiskniKontakty(char kontakty[MAX_CONTACT_COUNT][MAX_CHAR_COUNT], int * kontaktSplnujeFiltr) {
    //Vytiskneme na základě filtru
    for (int i = 0; i < MAX_CONTACT_COUNT; i++){
        if (kontaktSplnujeFiltr[i])
            printf("%s\n", kontakty[i]);
    }
}

int main(int argc, char ** argv){
    
    char kontakty[MAX_CONTACT_COUNT][MAX_CHAR_COUNT];
    int pocetKontaktu = 0;
    
    if (!nactiKontaktyZeVstupu(kontakty, &pocetKontaktu)){
        return 1;
    }
   
    //Bez parametrů, tiskneme rovnou
    if (argc == 1) {
        vytiskniVse(kontakty, pocetKontaktu);
        return 0;
    }
    
    if (argc > 2) {
        printf("Prilis mnoho parametru.\n");
        return 1;
    }
    
    char * filtr = argv[1];
    int kontaktSplnujeFiltr[MAX_CONTACT_COUNT] = {0};
    //kontaktSplnujeFiltr[0] = 1;
    
    if (!vyfiltrujKontakty(kontakty, pocetKontaktu, filtr, kontaktSplnujeFiltr)){
        printf("Not found\n");
        return 0;
    }
    
    vytiskniKontakty(kontakty, kontaktSplnujeFiltr);
    
    return 0;
}
