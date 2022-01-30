#lang racket
(require compatibility/defmacro)

(define-macro (? cond true : else )
  `(if ,cond
      ,true
      ,else
  )
)

; intersect - dva vzestupně seřazené listy najít průnik množin
; (intersect '(1 3 4) '(0 1 4 5)) -> '(1 4)
; (intersect '(1 2 3) '()) -> '()

(define (intersect lst1 lst2)
  (if (or (null? lst1) (null? lst2))
      null
      (let [ ( head1 (car lst1) ) ( head2 (car lst2) ) ]
        (cond
          ( (= head1 head2) (cons head1 (intersect (cdr lst1) (cdr lst2))) )
          ( (< head1 head2) (intersect (cdr lst1) lst2) )
          ( #T (intersect lst1 (cdr lst2)) )
        )
      )
  )
)

; 3b divisor x- najde všechny dělitele čísla a vypíše je vzestupně.

(define (my-mod x m)
  (if (< x m)
      x
      (my-mod (- x m) m)
  )
)

(define (divisor x)
  (my-divisor x 1)
)

(define (my-divisor x i)
  (cond
    ( (= i x) (cons x null) )
    ( (= (my-mod x i) 0) (cons i (my-divisor x (+ i 1))) )
    ( #T (my-divisor x (+ i 1)) )
  )
)

; 1b common-divisor a b - najde všechny společné dělitele čísel a b.

(define (common-divisor a b)
  (intersect (divisor a) (divisor b))
)
