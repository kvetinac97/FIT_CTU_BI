# Závěrečná úloha

Závěrečná úloha se skládá ze tří nezávislých úloh. Každá úloha má několik částí, za které můžete získat body. K úlohám
jsou napsané testy a vaším cílem je vytvořit implementaci podle zadání, která bude splňovat testovací podmínky.

Každá úloha má svůj kód ve vlastním namespace `FinalTask\NazevUlohy`. Kód naleznete v podložkách složky `src`, podle
názvu úlohy.

Projekt dále obsahuje testy v namespace `FinalTask\Tests`. Testy můžete použít pro lepší pochopení zadání. **Nesmíte je
ale jakkoliv upravovat.**

Ve složce `bin` jsou skripty pro vyhodnocování úloh, o ty se nemusíte vůbec starat a také je nesmíte měnit.

## Příprava

Projekt obsahuje composer skripty s několika příkazy, které vám usnadní některé kroky. Předtím, než začnete pracovat,
nainstalujte závislosti pomocí příkazu:

```bash
$ composer install
```

## Spouštění

Chcete-li si spouštět průběžné řešení, napište si vlastní run.php nebo obdobný spouštěcí skript.

## Testování

Pro spuštění testů můžete použít následující příkaz

```bash
$ composer test
``` 

Pokud chcete spouštět testy pro konkrétní úlohu nebo konkrétní TestCase můžete použít přímo phpunit:

```bash
$ vendor/bin/phpunit src/Tests/NazevUlohy
$ vendor/bin/phpunit src/Tests/NazevUlohy/KonkretniTest.php
```

## Hodnocení

Každá úloha a její části mají přidělený určitý počet bodů. Výsledné hodnocení bude přiděleno na základě splněných úloh v
testech, může být případně upraveno vyučujícím. Hodnocení jednotlivých úloh a částí se dozvíte pomocí příkazu:

```bash
$ composer evaluate
```

Další příkaz slouží k ověření, že nebyly provedeny žádné změny v testech nebo skriptech pro vyhodnocování výsledků.

```bash
$ composer checksum
```

**V případě, že checksum nebude odpovídat, bude celá závěrečná úloha hodnocena automaticky 0 body.**

## Úlohy

### 1) Trojúhelník

Třída `Triangle` reprezentuje trojúhelník, který má strany a, b, c.

1. Implementujte konstruktor a gettery pro strany a, b, c.
1. Pokud vstupní hodnoty porušují pravidlo o vzájemné délce stran a netvoří trojúhelník vyvolejte výjimku `\Exception`
2. Pokud je některá vstupní hodnota záporná vyvolejte výjimku `\Exception`
1. Implementujte metodu `isRight`, která vrací zda je trojúhelník pravoúhlý.
1. Implementujte metodu `isEquilateral`, která vrací zda je trojúhelník rovnostranný.
1. Implementujte metodu `isIsosceles`, která vrací zda je trojúhelník rovnoramenný.
1. Implementujte statickou metodu `Triangle::similar`, která má dva parametry typu `Triangle` a vrací, zda jsou
   trojúhelníky podobné tj. mají velikosti stran ve stejném poměru.

### 2) Statistika testování COVID-19

Třída `Covid\CovidStats` obsahuje jedinou statickou metodu `getMostTestedIntervals`. Vaším úkolem je tuto metodu
implementovat. Ostatní třídy umístění ve složce Covid jsou podpůrné struktury, které není potřeba měnit (můžete je
rozšířit, ale neměňte jejich rozhraní).

Tato metoda načítá data z formátu JSON a počítá, kolik testů proběhlo v různých sedmidenních intervalech.
Metoda vrací tři intervaly s nejvyšším počtem testů. 

* Metoda přijímá argument `from` a `to`, oba ve formátu `YYYY-mm-dd`, tedy například `2020-04-17`. Tato data ohraničují
  rozsah, ve kterém má metoda statistiku počítat.
  * Vstup je potřeba validovat následujícími pravidly, při jejich porušení vyhoďte výjimku `\InvalidArgumentException`:
    * `from` musí být nižší než `to`,
    * rozpětí mezi `from` a `to` musí být alespoň 9 dní (včetně krajních hodnot),
    * `from` musí být vyšší nebo rovno `2020-01-27`,
    * `to` musí být nižší nebo rovno `2020-12-15`.
* Můžete očekávat následující (nemusíte to ošetřovat a hlídat):
    * zdrojový soubory existuje,
    * data ve zdrojovém souboru na sebe plynule navazují (obsahují postupně všechny dny mezi 27. 1. a 15. 12. 2020),
      jsou seřazená,
    * parametry `from` a `to` jsou do metody poslané ve správném formátu (není nutné je validovat).
* Metoda načte zdrojová data ze složky `data` a vypočítá **tři sedmidenní intervaly**, ve kterých proběhlo nejvíce
  testů (ve zvoleném rozsahu mezi `from` a `to`).
    * Interval nemusí být pouze pondělí - neděle, ale libovolných 7 po sobě jdoucích dnů!
    * Počet testů v daném dni je ve zdrojových datech označen klíčem **prirustkovy_pocet_testu**.
    * Výsledné intervaly budou seřazeny podle celkového počtu provedených testů v tomto sedmidenní, nejvyšší počet nejdříve.

### 3) Events

Třída `Event` reprezentuje koncert, má id, název, datum a místo konání a umí se ukládat do databáze. Pro práci s
databází použijte třídu `PDO`, která je ve třídě `Event` dostupná v proměnné `$pdo`.

1. Implementujte statickou metodu `createDbTable`, která vytvoří v databázi tabulku pro ukládání knih
   pojmenovanou `events`, pokud ještě neexistuje. Tabulka musí obsahovat následující sloupce:
    1. `id INTEGER`
    1. `title TEXT`
    1. `date TEXT`
    1. `venue TEXT`
1. Implementujte metodu `save`, která uloží instanci `Event` do databáze tak, že namapuje příslušné proměnné na
   příslušné sloupce v tabulce.
    1. Pokud instance s daným `id` již existuje, aktualizuje se existující záznam.
    1. Pokud instance s daným `id` neexistuje (také když `id` není nastaveno), uloží se jako nový záznám a `id` se
       vygeneruje. Můžete ho vygenerovat buď v PHP nebo na úrovni databáze.
1. Implementujte statickou metodu `findById`, která má jeden parametr `id` a která v databázi najde knihu podle id a
   vrátí její instanci.
    1. Pokud záznam s daným `id` neexistuje vrátí `null`.
1. Implementujte statickou metodu `findByVenueAndDate` s parametrem `venue` a volitelným parametrem `date`, která v
   databázi najde všechny záznamy s daným místem konání v daný datum a vrátí je jako pole instancí třídy `Event`.
    1. Pokud není datum `date` zadán, hledá se pouze podle místa konání.
    1. Pokud žádné vyhovující záznamy neexistují, vrátí prázdné pole.
