#include<stdio.h>

int isPalindrome(int r, long long int i, int print);
long long int createPalindrome(int r, long long int prefix, long long int length);

long long int countPalindromes(int r, long long int x);
void displayPalindroms(int r, long long int low, long long int high);

/*
 * Hlavní funkce programu
 */
int main(void){
    
    char x;
    int n = 0, r;
    long long low, high;
    
    printf("Vstupni intervaly:\n");
    
    while (n != EOF) {
        
        n = scanf(" %c %d %lld %lld ", &x, &r, &low, &high);
        if (n == EOF)
            continue;
        
        if (n != 4 || (x != 'l' && x != 'c') || r < 2 || r > 36 || low < 0 || low > high){
            printf("Nespravny vstup.\n");
            return 1;
        }

        if (x == 'c') {
            
            //Počet palindromů spočítáme jako rozdíl palindromů 1..high - 1..low-1
            long long int count = countPalindromes(r, high) - countPalindromes(r, low - 1);
            printf("Celkem: %lld\n", count);
            
        }
        else
            displayPalindroms(r, low, high);
        
    }
    
    return 0;
    
}

/*
 * Spočítá počet palindromů od 1 do x
 * Složitost: O (log n)
 */
long long int countPalindromes(int r, long long int x){
    
    //Nejprve spočítáme délku čísla v dané soustavě
    int length = 0;
    long long int tempX = x;

    for (length = 0; tempX > 0; tempX /= r)
        length++;
    
    //Pro výpočet počtů palindromů využijeme první část (větší půlku) čísla
    int firstPartLength = (length + 1) / 2;
    
    //Vypočteme první část čísla v soustavě r
    long long int firstPart = x;
    
    for (int i = 0; i < (length - firstPartLength); i++)
        firstPart /= r;
    
    //Pokud je palindrom vytvořený první částí větší než originální číslo
    // 1200 -> 12 -> 1212 > 1200
    
    //tak zmenšíme první část o 1
    // 11 -> 1111 < 1200
    if (createPalindrome(r, firstPart, length) > x)
        firstPart--;
    
    //Počet palindromů je nyní tedy dvojnásobek velikosti první části
    //př. z 11 můžeme vytvořit 1111 nebo 111
    firstPart *= 2;
    
    //Počet ale musíme přizpůsobit tomu, že pro liché číslo máme palindromů příliš
    //12345 -> 123 -> vytvoří i palindromy větší než x
    //a pro sudé číslo je naopak palindromů málo
    //1234 -> 12 -> vytvoří i palindromy menší než x
    
    int modification = 1;
    
    for (int i = (length & 1 ? 1 : 0); i < firstPartLength; i++)
        modification *= r;
    
    if (length & 1)
        firstPart -= (firstPart / 2 - modification + 1);
    else
        firstPart += (modification - 1 - firstPart / 2);

    //Počet palindromů je nyní správný
    return firstPart;
    
}

/*
 * Zobrazí všechny palindromy v intervalu <low;high> v soustavě r
 * Složitost O(n)
 */
void displayPalindroms(int r, long long int low, long long int high){
    
    for (long long int i = low; i <= high; i++)
        if (isPalindrome(r, i, 0))
            isPalindrome(r, i, 1);
    
}

/*
 * Kontrola, zda je číslo i v soustavě r palindromem
 * Parametr print slouží k vytištění čísla v případě potřeby
 */
int isPalindrome(int r, long long int i, int print){
    
    long long int num = i, revNum = 0;
    int digit = 0;
    
    if (print)
        printf("%lld = ", i);
    
    //Pro nulu rovnou vytiskneme nulu
    if (i <= 0 && print)
        printf("%d", 0);
    
    //Převodem do dané soustavy zároveň ověříme, zda je číslo palindrom
    //Složitost O(log n)
    while (i != 0) {
        
        digit = i % r;
        revNum = (revNum * r) + digit;
        i /= r;
        
        if (print) {
            
            if (digit > 9)
                printf("%c", 87 + digit);
            else
                printf("%d", digit);

        }
        
    }
    
    if (print)
        printf(" (%d)\n", r);
    
    return (num == revNum);
    
}

/*
 * Vytvoření palindromu v soustavě r na základě jeho první části
 */
long long int createPalindrome(int r, long long int prefix, long long int length){
    
    long long int firstPart = prefix;
    long long int secondPart = 0;

    //U liché délky čísla přeskočíme poslední číslici
    if (length & 1)
        prefix /= r;

    //Převod do dané soustavy využitý k vytvoření palindromu
    //Složitost O(log n)
    while (prefix != 0) {
        
        long long int digit = prefix % r;

        prefix /= r;
        secondPart = (secondPart * r) + digit;
        firstPart *= r;
        
    }
    
    //Vrátíme složení obou částí
    return firstPart + secondPart;
    
}
