# Úkol 3 - Třídy

## 1. Třída Bag

Implementujte třídu Bag, která se nachází v `classes/Bag.php`. Třída má 7 metod:

- `add($item)` - přidá prvek do kolekce, nevrací nic
- `clear()` - vyprázdní kolekci
- `contains($item)` - vrací `true` pokud se prvek nachází v kolekci, `false` v opačném případe
- `elementSize($item)` - vrací počet výskytů daného prvku
- `isEmpty()` - vrací `true` pokud je kolekce prázdná, `false` v opačném případě
- `remove()` - odebere prvek z kolekce, pokud tam prvek není, nestane se nic. Pokud je tam vícekrát, odebere se pouze jeden výskyt
- `size()` - vrací celkový počet prvků v kolekci


## 2. Autoloader

V souboru `main.php` je příklad použití (můžete využít k lepšímu pochopení zadání). Implementuje a zaregistrujte autoloader, který zajistí načítání tříd ze složky `classes`.


## 3. Třída Set

Vytvoře novou třídu `Set` jako potomka třídy `Bag`, která se chová následovně:

- `add($item)` - přidá prvek do kolekce, v případě, že už se tam prvek nachází, nestane se nic
- `clear()` - vyprázdní kolekci
- `contains($item)` - vrací `true` pokud se prvek nachází v kolekci, `false` v opačném případe
- `elementSize($item)` - vrací počet výskytů daného prvku, v případě `Set` je to 0 nebo 1
- `isEmpty()` - vrací `true` pokud je kolekce prázdná, `false` v opačném případě
- `remove()` - odebere prvek z kolekce
- `size()` - vrací celkový počet prvků v kolekci

Funkcionalita se částečně shoduje s třídou `Bag`, **snažte se tedy neimplementovat vše znovu, ale využít vlastností OOP ke sdílení funkcionality.** - nedodržení tohoto požadavku může vést ke stržení bodů


## 4. Kontrola správnosti

Až budete mít vše implementované, zkuste soubor spustit a ověřit, že se vše chová správně. V takovém případě skript nevypíše nic, jinak uvidíte warningy.

```
$ php main.php
```
