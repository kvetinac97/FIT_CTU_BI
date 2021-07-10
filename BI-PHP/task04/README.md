# Úkol 4

Ve složce src je třída `Node`, která reprezentuje uzel binárního stromu. Každý uzel může mít nějakou číselnou hodnotu a levého a pravého potomka. Pokud uzel nemá některého potomka má atribut hodnotu `null` v opačném případě obsahuje referenci na jiný uzel `Node`.

Cílem je vytvořit implementace iterátorů (interface [Iterator](https://www.php.net/manual/en/class.iterator.php)) pro průchod stromu v pořadí

- in-order,
- pre-order a
- post-order.

Iterátor zajistí, že strom lze procházet např. pomocí `foreach`. 

## Zadání

1. Implementujte a zaregistrujte autloader, který zajistí správně načítání tříd, včetně namespaců ze složky `src`. (1 bod)
1. Doplňte metody `Iterator\InOrderIterator` pro **in-order** průchod. (2 body). `Iterator\AbstractOrderIterator` si můžete upravit, jak budete potřebovat, při zachování `\Iterator` interface.
1. Doplňte metody `Iterator\PreOrderIterator` a `Iterator\PostOrderIterator` pro **pre-order** resp. **post-order** průchod. Snažte se sdílet společný kód prostřednictvím dědičnosti. (2 body) 
1. Upravde třídu `Node`, aby implementovala [IteratorAggregate](https://www.php.net/manual/en/class.iteratoraggregate.php), ve výchozím stavu bude používat `InOrderIterator`. (2 body)

Kromě čtvrtého bodu **neměňte** třídu `Node`.
