# Úkol 6: Bankovní transakce

Cílem je vytvořit program, který bude importovat bankovní transakce ze souboru do databáze a vypisovat souhrn pro jednotlivé bankovní účty. Uložení do databáze zajistí persistenci dat mezi jednotlivými spuštěními aplikace.

Máte připravenu kostru aplikace a úkolem bude doplnit implementace určených metod.

## Třída `Db`

Obsahuje jedinou statickou metodu `get`, která realizuje a reprezentuje spojení s SQLite databází pomocí PDO.

## Model `Account`

Obsahuje datový model pro bankovní účet, instance odpovídá řádku v databázi, třída odpovídá tabulce. Atribut `id` je jednoznačný - databází automaticky generovaný - identifikátor; v databázi primární klíč.
Zbylé atributy `number` a `code` jsou číslo účtu resp. kód banky.
 
## Model `Transaction`

Datový model pro transakci mezi dvěma bankovními účty. Transakce je převod částky `amount` z účtu `from` na účet `to`.
V PHP jsou atributy `from` a `to` referencemi na objekt `Account`; v databázi bude vazba reprezentovaná pomocí cizího klíče.
Atribut `id` je jednoznačný - databází automaticky generovaný - identifikátor; v databázi primární klíč.

## `data`
V adresáři `data` naleznete dva soubory s transakcemi ve formátu
```
cislo_uctu kod_banky cislo_uctu kod_banky castka
``` 

## Implementujte

1. statické metody `Account::createTable`, `Transaction::createTable`, `Account::dropTable`, `Transaction::dropTable` - metody slouží k inicializaci (resetování) databáze. Použijte volání odpovídajícího SQL příkazu.

1. statickou metodu `Account::find` - která vrátí (vytvoří) instanci `Account` pro odpovídající řádek  z databázové tabulky. V případě nenalezení řádku vrátí `null`.

1. statickou metodu `Account::findById` - která vrátí (vytvoří) instanci `Account` pro odpovídající primírní klíč (id) z databázové tabulky. V případě nenalezení řádku vrátí `null`.

1. statickou metodu `Account::findOrCreate` - která vrátí (vytvoří) instanci podle existujícího řádku nebo podle nově vloženého.

1. instanční metodu `$account->getTransactions` - která vrátí pole instancí `Transaction` související s účtem (převod z/na daný účet).

1. instanční metodu `$account->getTransactionSum` - která spočítá aktuální stav účtu (tzn. pro transakce z metody výše) pomocí databázové agregační funkce. Odchozí transakce odečítá, příchozí přičítá.

1. instanční metodu `$transaction->insert`, která vloží data aktuální instance do databáze.

Neupravujte ostatní soubory.

## Ověření implementace

Aplikace se používá pomocí skriptu `run.php`, který přijímá argumenty z příkazové řádky.
- Příkaz `init` slouží k inicializaci (resetování) stavu databáze - odstraní a znovu vytvoří tabulky.
- Příkaz `import <soubor>` importuje transakce ze souboru
- Příkaz `summary <cislo uctu> <kod banky>` zobrazí transakce a součet pro zadaný účet

Vaší implementaci můžete ověřit pomocí shellového skriptu `test.sh`, který porovná očekávané výstupy s výstupy vaší implementace. Skript zobrazuje diff Vašeho a očekávaného výstupu. Pokud je program zcela správně, mělo by na výstupu být:

```
== init
== import tr1
== summaries
Files test/111300_0710_1.txt and - are identical
Files test/111323_0710_1.txt and - are identical
Files test/330330_0800_1.txt and - are identical
Files test/330331_0800_1.txt and - are identical
== import tr2
== summaries
Files test/100100_0300_2.txt and - are identical
Files test/100100_0710_2.txt and - are identical
Files test/101010_2010_2.txt and - are identical
Files test/111300_0710_2.txt and - are identical
Files test/111323_0710_2.txt and - are identical
Files test/330330_0800_2.txt and - are identical
Files test/330331_0800_2.txt and - are identical
Files test/330335_0800_2.txt and - are identical
```