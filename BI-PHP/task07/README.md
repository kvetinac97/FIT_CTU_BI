# Úkol 7: Testování

## Zadání

### MathUtils

Třída `MathUtils` obsahuje několik matematických funkcí, které mohou obsahovat chyby. Ve tříde `Tests\MathUtilsTest` napište testy, které ověří správnou funkcionalitu. V případě nalezení chyby v implementaci ji opravte. Nezapomeňte otestovat všechny případy.


### UserService

Třída `UserService` ukládá uživatele a načítá o nich data. Používá k tomu interface `Storage`, kde implementace není známá a který se předává do konstruktoru. Napište testy, které ověří funkcionalitu `UserService`, vytvořte mock pro `Storage`, který v testech použijete.

### LinkedList, LinkedListItem

Třída `LinkedList` představuje obousměrně zřetězený seznam položek `LinkedListItem`. Instance `LinkedList` obsahuje referenci na první a poslední prvek, instance `LinkedListItem` na předchozí a následující.
Metody pro vkládání prvků ve třídě `LinkedList` mohou obsahovat chyby. Ve tříde `Tests\LinkedListItemTest` napište testy, které ověří správnou funkcionalitu. V případě nalezení chyby v implementaci ji opravte. Nezapomeňte otestovat všechny případy - spojitost seznamu v obou směrech, správné pořadí položek atd.

## Instrukce

Napište testy, které ověří funkcionalitu ve všech případech, tak aby code coverage bylo 100%. Ke spuštění testů můžete využít připravené composer scripty:

```
$ composer test
```

Pro zjištění code coverage můžete použít:

```
$ composer coverage
```
Vytvoří se složka `coverage`, ve které naleznete HTML report. Pro spuštění phpunit s code coverage je potřeba mít v PHP extension [xdebug](https://xdebug.org/docs/install).

Report pokrytí kódu testy musí pro plný zisk bodů být 100%.
