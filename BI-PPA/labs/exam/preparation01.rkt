#lang racket

; (foldr foo init lst)

(define (foldr foo init lst)
  (if (null? lst)
      init
      (foo (foldr foo init (cdr lst)) (car lst))
  )
)

(define (my-count elem lst)
  (cadr (foldr (lambda (x y) (cons (car x) (cons (+ (cadr x) (if (equal? (car x) y) 1 0)) null))) `(,elem 0) lst))
)

(define (remove-all elem lst)
  (cdr (foldr (lambda (x y) (if (equal? (car x) y) x (cons (car x) (cons y (cdr x))))) `(,elem) lst))
)

(define (counter lst)
  (my-counter '() lst)
)

(define (my-counter aux lst)
  (if (null? lst)
      aux
      (my-counter (cons (cons (car lst) (cons (my-count (car lst) lst) null)) aux) (remove-all (car lst) lst))
  )
)