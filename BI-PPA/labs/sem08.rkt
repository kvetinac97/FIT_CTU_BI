#lang racket

; Rekurzivně umíme součet, délku ...
(define (my-sum lst)
  (if (null? lst)
      0
      (+ (car lst) (my-sum (cdr lst)))
  )
)

(define (my-len lst)
  (if (null? lst)
      0
      (+ 1 (my-len (cdr lst)))
  )
)

; Tyto funkce jsou docela podobné ...
; co si usnadnit práci?

(define (my-foldr lst action init)
  (if (null? lst)
      init
      (action (car lst) (my-foldr (cdr lst) action init))
  )
)

; touto funkcí můžeme snadno přepsat cokoliv

(define (my-sum-x x) (my-foldr x + 0))
(define (my-fac-x x) (my-foldr x * 1))
(define (my-len-x x) (my-foldr x (lambda (e acc) (+ 1 acc)) 0))

(define (my-min-x x) (my-foldr x (lambda (e acc) (if (< e acc) e acc)) (car x)))
(define (my-max-x x) (my-foldr x (lambda (e acc) (if (> e acc) e acc)) (car x)))

; zkusme spočítat průměr
(define (my-avg lst)
  (let [(avg-cell 
            (my-foldr lst
                (lambda (e res) (cons (+ e (car res)) (+ 1 (cdr res))) )
                (cons 0 0)
            )
        )]
  (/ (car avg-cell) (cdr avg-cell)))
)

; foldr je hezké, ale my chceme foldl

(define (my-foldl lst action init)
  (if (null? lst)
      init
      (my-foldl (cdr lst) action (action init (car lst)))
  )
)

(define (my-append lst e)
  (my-foldr lst cons `(,e))
)

(define (my-reverse lst)
  (my-foldl lst (lambda (e res) (cons res e)) null)
)

(define (my-map lst map-fn)
  (my-foldr lst (lambda (e res) (cons (map-fn e) res)) null)
)

(define (my-listize lst)
  (my-map lst (lambda (x) (cons x null)))
)

; Co nějaká funkce na filtrování

(define (my-filter lst condition)
  (my-foldr lst (lambda (e res) (if (condition e) (cons e res) res)) null)
)

(define (my-range i n)
  (if (> i n)
      null
      (cons i (my-range (+ 1 i) n))
  )
)

(define (my-sieve lst)
  (if (null? lst)
      null
      (cons (car lst) (my-sieve (my-filter lst (lambda (x) (not (zero? (modulo x (car lst))))))))
  )
)

; Reprezentace matice
(define (my-mtx-1) '((1 2) (3 4)))
(define (my-mtx-2) '((6 7) (8 9)))

(define (my-mtx-sum m1 m2)
  (my-foldr (map list m1 m2) (lambda (e res) (cons (map + (car e) (cadr e)) res)) null)
)

; (my-mtx-sum (my-mtx-1) (my-mtx-2))

; Jednoduše:
(define (my-mtx-sum-easy m1 m2)
  (map (lambda (x y) (map + x y)) m1 m2)
)
