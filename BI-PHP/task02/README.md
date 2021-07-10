# Úkol č.2 Nákupní seznam

## Zadání

Vaším úkolem je implementovat tři funkce ve skriptu `shopping.php`.

První funkce `getPrice` dostane na vstup řetězec (příklady vizte níže) a vrátí číslenou hodnotu, která bude vyjadřovat cenu položky.

Druhá funkce `sortList` dostane na vstup pole řetězců (položek) a vrátí ho seřazené podle ceny vzestupně.

Třetí funkce `sumList` dostane na vstup pole řetězců (položek) a vrátí číselnou hodnotu představující celkovou cenu.

## Struktura projektu

- `input` - adresář se vzorovými vstupními daty
- `shopping.php` - skript, ve kterém máte za úkol implementovat výše zmíněné funkce

## Spuštění

Ke spuštění a otestování můžete využít příklady ze složky input.

```
$ php shopping.php input/1.txt
```

## Popis vstupu

- Na každém řádku se nachází jedna položka nákupního seznamu.
- Částka je uvedena jako číslo s desetinnou čárkou, tisíce jsou odděleny tečkou. Desetinná část je nepovinná.
- Označení měny je buď `Kč` nebo `CZK`, případně je-li částka celé číslo bez halířů může být místo měny uvedeno `,-`.
- Umístění `CZK` může být před nebo za částkou, `Kč` nebo `,-` se vyskytuje jen za částkou.
- Měna může ale nemusí být oddělena mezerou.

```
Rohlík 5Kč
CZK400 Knížka
Pivo 42,-
Houska 4 Kč
Máslo 49,00 Kč
Herní konzole 4.900 CZK
Rádio CZK550
CZK 1.600,59 Natural 95
```
