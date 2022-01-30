#lang racket

; (cetnost 'a '(b a b a ((a)) (a b c))) -> 4
; (cetnost 'x '(b a b a ((a)) (a b c))) -> 0

(define (cetnost value lst)
  (if (null? lst)
      0
      (let [ (rest (cetnost value (cdr lst))) ]
         (if (pair? (car lst))
             (+ (cetnost value (car lst)) rest)
             (+ (if (equal? value (car lst)) 1 0) rest)
         )
      )
  )
)

; (vyskyty '(a b a (b c b a) (a) (b))) -> '((a 4) (b 4) (c 1))
(define (vyskyty lst)
  (if (null? lst)
      null
      (cons (cons (car lst) (cons (cetnost (car lst) lst) null))) (vyskyty (cdr lst)))
  )
)
