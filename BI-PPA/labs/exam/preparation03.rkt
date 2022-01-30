#lang racket

; sumNeighbours, která sčítá sousedy v listu.
; Př. Pokud mi přijde na vstupu seznam liché délky, sečtu co můžu
; a poslední prvek ponechám beze změny (sumNeighbours '(1 2 3 4 5)) → '(3 7 5)
; Př. Pokud sudé délky, sečtu normálně (sumNeighbours '(1 2 3 4 5 6)) → '(3 7 11)

(define (sumNeighbours lst)
  (cond
    ( (null? lst) null )
    ( (null? (cdr lst)) lst )
    ( #T (cons (+ (car lst) (cadr lst)) (sumNeighbours (cdr (cdr lst)))) )
  )
)

; napsat funkci reduce, která pomocí předchozí funkce (sumNeighbours)
; sečte všechny prvky v seznamu
(define (reduce lst)
   (cond
    ( (null? lst) 0 )
    ( (null? (cdr lst)) (car lst) )
    ( #T (reduce (sumNeighbours lst)) )
  )
)

; napsat funkci reduce2 (implementačně šlo spíš o foldLeft),
; která bude přijímat počáteční hodnotu a libovolnou porovnávací
; funkci jako parametr (zkrátka aby to umělo víc než sčítat).
; Tady už se nepoužívalo sumNeighbours.

; Př. (reduce2 '(1 2 3 4 5) + 0)) → 15

(define (reduce2 lst fn init)
  (if (null? lst)
      init
      (fn (car lst) (reduce2 (cdr lst) fn init))
  )
)