#lang racket

; Smazání všech výskytů prvku ze seznamu

(define (my-delete lst e)
  (if (null? lst)
      null
      (if (= (car lst) e)
          (my-delete (cdr lst) e)
          (cons (car lst) (my-delete (cdr lst) e))
      )
  )
)

; Smazání prvního výskytu prvku ze seznamu

(define (my-delete-first lst e)
  (if (null? lst)
      null
      (if (= (car lst) e)
          (cdr lst)
          (cons (car lst) (my-delete-first (cdr lst) e))
      )
  )
)

; Seřazení seznamu (merge-sort)

; (Spojení dvou seznamů)

(define (my-merge l1 l2)
  (cond
    ((null? l1) l2)
    ((null? l2) l1)
    ((< (car l1) (car l2)) (cons (car l1) (my-merge (cdr l1) l2)))
    (#t                    (cons (car l2) (my-merge l1 (cdr l2))))
  )
)

; Rozdělení seznamu na dva skoro stejné délky

(define (my-split lst)
  (my-split-aux lst null null)
)

(define (my-split-aux lst l1 l2)
  (if (null? lst)
      (cons l1 l2)
      (my-split-aux (cdr lst) l2 (cons (car lst) l1))
  )
)

; Druhá možnost = slow pointer, fast pointer
(define (my-split-fast lst)
  (my-split-fast-aux lst lst)
)

(define (my-split-fast-aux slow fast)
   (if (or (null? fast) (null? (cdr fast)))
       (cons null slow)
       (let*
           [(res     (my-split-fast-aux (cdr slow) (cddr fast)))
            (first  (car res))
            (second (cdr res))]
           (cons
             (cons (car slow) first)
             second
           )
       )
   )    
)

; Merge-sort :POG

(define (my-mergesort lst)
  (if (or (null? lst) (null? (cdr lst)))
      lst
      (let [(split (my-split lst))]
        (my-merge (my-mergesort (car (my-split-fast lst)))
                (my-mergesort (cdr (my-split-fast lst)))
        )
      )
  )
)

; Je číslo prvočíslem?

(define (my-prime n)
  (if (< n 2)
      #f
      (my-prime-aux n 2)
  )
)

(define (my-prime-aux n k)
  (if (<= n k)
      #t
      (if (= 0 (modulo n k))
          #f
          (my-prime-aux n (+ k 1))
      )
  )
)

; Napojení seznamů

(define (my-append l1 l2)
  (if (null? l1)
      l2
      (cons (car l1) (my-append (cdr l1) l2))
  )
)

; Odstranění podseznamů

(define (my-flatten lst)
  (cond
    ((null? lst) null)
    ((atom (car lst)) (cons (car lst) (my-flatten (cdr lst))))
    (#t                (my-append (my-flatten (car lst)) (my-flatten (cdr lst))))
  )
)

; Je to atom?

(define (atom x)
  (and (not (pair? x)) (not (null? x)))
)

; Binární stromeček
; '() nebo '(val bst-l bst-r)

(define (bst-inorder bst)
  (if (null? bst)
      null
      (my-append-3 (bst-inorder (cadr bst)) (car bst) (bst-inorder (caddr bst)))
  )
)

(define (my-append-3 l1 atom l2)
  (my-append l1 (cons atom l2))
)

(define strom
  '(10
    (5 () (7
             (6 () ())
             (9 () ())
            )
     )
    (15 () ())
   )
)

; Vložení do stromu

(define (bst-insert bst e)
  (if (null? bst)
      (bst-construct e null null)
      (if (< e (car bst))
          (bst-construct (bst-insert (cadr bst) e) (car bst) (caddr bst))
          (bst-construct (cadr bst) (car bst) (bst-insert (caddr bst) e))
      )
  )
)

(define (bst-construct val left right)
  (cons val (cons left (cons right null)))
)