#lang racket
(require compatibility/defmacro)

; [2 b.] Vytvořte makro (my-let id val body), které emuluje let z Racketu.
; Př.: > (my-let x 5 (* (+ x 1) x)) → 30.

(define-macro (my-let id val body)
  `((lambda (,id) ,body) ,val)
)

; Funkci split, která daný seznam rozdělí na dva podseznamy stejné délky
(define (split lst)
  (split-aux lst null null)
)

(define (split-aux lst aux1 aux2)
  (cond
    ( (null? lst) (cons aux1 aux2) )
    ( (null? (cdr lst)) (split-aux null (cons (car lst) aux1) aux2) )
    ( #T (split-aux (cdr (cdr lst)) (cons (car lst) aux1) (cons (cadr lst) aux2) ) )
  )
)

(define (merge lst1 lst2)
  (cond
    ( (and (null? lst1) (null? lst2)) null )
    ( (null? lst1) lst2 )
    ( (null? lst2) lst1 )
    ( (<= (car lst1) (car lst2)) (cons (car lst1) (merge (cdr lst1) lst2)) )
    ( #T (cons (car lst2) (merge lst1 (cdr lst2))) )
  )
)

; [3 b.] Funkci mergesort, která daný seznam setřídí algoritmem MergeSort.
(define (merge-sort lst)
  (if (or (null? lst) (null? (cdr lst)))
      lst
      (my-let lists (split lst)
          (merge (merge-sort (car lists)) (merge-sort (cdr lists)))
      )
  )
)