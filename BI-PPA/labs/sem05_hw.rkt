#lang racket

; Inkrement
(define (inc n)
  (+ n 1)
)

; Dekrement
(define (dec n)
  (- n 1)
)

; Nulovost
(define (zero n)
  (= n 0)
)

; Součet pomocí increment/decrement
(define (add x y)
  (if (zero x)
      y
      (add (dec x) (inc y))
  )
)

; Součin pomocí sčítání
(define (mult x y)
  (mult-aux x y 0)
)

(define (mult-aux x y acc)
  (if (zero x)
      acc
      (mult-aux (dec x) y (add acc y))
  )
)

; Délka seznamu včetně podseznamů
(define (lstlen x)
  (if (null? x)
      0
      (if (list? (car x))
          (+ (one-or-zero (lstlen (car x))) (lstlen (cdr x)))
          (+ 1 (lstlen (cdr x)))
       )
  )
)

(define (one-or-zero x)
  (if (= x 0)
      1
      x
  )
)

; Fibonacciho číslo

(define (my-fib n)
  (cond
    ((= n 0) 0)
    ((= n 1) 1)
    (#t (+ (my-fib (- n 1)) (my-fib (- n 2))))
  )
)

; n-tý prvek v seznamu

(define (my-nth list n)
  (if (= 0 n)
      (car list)
      (my-nth (cdr list) (- n 1))
  )
)

; prvočíselnost

(define (my-prime n)
  (my-prime-aux n 2)
)

(define (my-prime-aux n i)
  (if (= i n)
      #t
      (if (= 0 (mod n i))
          #f
          (my-prime-aux n (+ i 1))
      )
  )
)

(define (mod n i)
  (if (< n i)
      n
      (mod (- n i) i)
  )
)