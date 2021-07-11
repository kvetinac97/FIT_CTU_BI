## Úloha 2

Napište program, který nalezne libovolnou zprávu, jejíž **hash (SHA-384) začíná** zleva na posloupnost nulových bitů:

* Počet nulových bitů je zadán celým číslem jako parametrem na příkazové řádce.
* Pořadí bitů je big-endian: Bajt 0 od MSB do LSB, Bajt 1 od MSB do LSB, ..., poslední bajt od MSB do LSB.
* Nalezenou **zprávu vypište v hexadecimální podobě**.