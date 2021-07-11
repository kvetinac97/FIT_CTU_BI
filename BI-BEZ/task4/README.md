## Úloha 4

Napište dva programy:

* První program zašifruje zprávu (soubor) pomocí kombinace symetrické a asymetrické šifry (`EVP_Seal`...) pro příjemce (Vás nebo souseda).
    * Vstupem budou 2 soubory:
        * Soubor s daty (binární data obecné velikosti)
        * Soubor s veřejným klíčem adresáta
    * Výstupem bude 1 soubor obsahující vše, co je nutné pro dešifrování soukromým klíčem adresáta.
        * (šifrový text, typ symetrické šifry (včetně operačního módu a délky klíče), inicializační vektor a zašifrovaný symetrický klíč)
    * Jména souborů budou zadána formou parametrů na příkazové řádce.
* Druhý program  dešifruje zprávu (soubor) pomocí kombinace symetrické a asymetrické šifry (`EVP_Open`...) z předchozího bodu a uloží do souboru.
* Porovnejte vstupní a výstupní (dešifrovaný) soubor na binární shodu.
* Na začátku zdrojového kódu napište do komentáře jméno autora.