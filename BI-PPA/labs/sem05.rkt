#lang racket

; Ukázková funkce na maximum
(define (my-max a b)
  (if (> a b)
      a
      b
  )
)

; Špatný faktoriál
(define (fact n)
  (if (equal? 0 n)
      1
      (* n (fact (- n 1)))
  )
)

; Faktoriál dobře = koncová rekurze
(define (fact2 n)
  (fact2-help n 1)
)

(define (fact2-help n acc)
  (if (equal? 0 n)
      acc
      (fact2-help (- n 1) (* n acc))
  )
)

; Pozor na cons, ať uděláme seznam
(define (badCons) (cons 1 2))

(define (correctCons)
  (cons 1 (cons 2 null))
)

; Spočítáme délku seznamu
(define (my-sum list)
  (my-sum-aux list 0)
)

(define (my-sum-aux list acc)
  (if (null? list)
      acc
      (my-sum-aux (cdr list) (+ (car list) acc))
  )
)

; Další naše funkce
(define (my-min list)
  (if (null? (cdr list))
      (car list)
      (min (car list) (my-min (cdr list)))
  )
)

; Ukázka funkce cond
(define (showcase-cond list)
  (cond
    ((null? list) 0)
    ((null? (cdr list)) 1)
    (#t 2) ; odpovídá "else"
  )
)

; Zkusme vygenerovat čísla od 0 do n
(define (my-range n)
  (my-range-aux n `())
)

(define (my-range-aux n acc)
  (if (equal? n 0)
      acc
      (my-range-aux (- n 1) (cons n acc))
  )
)

; Přidání na konec seznamu
(define (my-append list n)
  (if (null? list)
      (cons n null)
      (cons (car list) (my-append (cdr list) n))
  )
)

; Otočení seznamu (pomalu)
(define (my-reverse-bad list)
  (if (null? list)
      null
      (my-append (my-reverse-bad (cdr list)) (car list))
  )
)

; Otočení seznamu (dobře)
(define (my-reverse list)
  (my-reverse-aux list '())
)

(define (my-reverse-aux list acc)
  (if (null? list)
      acc
      (my-reverse-aux (cdr list) (cons (car list) acc))
  )
)