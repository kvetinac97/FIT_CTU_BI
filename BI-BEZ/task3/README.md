## Úloha 3

1. Stáhněte si obrázek ve formátu TGA
2. Napište program, který zkopíruje hlavičku a **zašifruje** část souboru s obrazovými daty pomocí AES v módu ECB. Výstupní soubor se bude jmenovat `(původní_jméno)_ecb.tga`.
3. Napište program, který **dešifruje** obrázek zašifrovaný prvním programem. Výstupní soubor se bude jmenovat `(původní_jméno)_dec.tga`.
   Porovnejte původní obrázek a jeho zašifrovanou podobu a vysvětlete svá zjištění.
4. Změňte pro šifrování i dešifrování použitý operační mód na *CBC* a vytvořte `(původní_jméno)_cbc.tga` a `(původní_jméno)_cbc_dec.tga` _(upřesní cvičící)_.
   Porovnejte původní obrázek a jeho zašifrovanou podobu a vysvětlete svá zjištění.
5. **Na první řádek zdrojáku dejte komentář se jménem autora**!

## Návod

 - `make` pouze zkompiluje program
 - `make run` spustí testy (ty lze spustit také manuálně pomocí `./test.sh`). Test zkusí každý obrázek ze složky `src` zašifrovat a dešifrovat (ECB, CBC módy) a následně porovná pomocí nástroje `cmp` přesnou binární shodu původního a dešifrovaného obrázku. Výsledné obrázky jsou k dispozici ve složce `dst`
 - `make clean` uvede projekt do původního stavu (smaže .o soubory, binárku programu) a smaže obsah složky `dst`

## Shrnutí

Jak si lze povšimnout, obrázek zašifrovaný v módu ECB stále připomíná původní obrázek. Proč je tomu tak? Mód ECB jednotlivé bloky šifruje zvlášť, tedy stejný blok (stejnou sadu barev vedle sebe) zašifruje vždy stejně, což vede k podobě obrázků. U módu CBC je již patrné, že jsou bloky otevřeného textu před šifrováním xorovány s předchozím šifrovým textem / inicializačním vektorem, a tak výsledný obrázek působí jako náhodný shluk barev.

