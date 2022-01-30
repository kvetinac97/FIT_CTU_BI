#lang racket

; Skalární součin (a * b = init + ∑(ai * bi)), tak aby operace + (tedy i ta sumovací) a * šli nahradit funkcemi f1 a f2
; scalar(a b init f1 f2)

(define (scalar a b init f1 f2)
  (if (null? a)
      init
      (f1 (scalar (cdr a) (cdr b) init f1 f2) (f2 (car a) (car b)))
  )
)

; bvs-insert(x tree) - vložit prvek x do BVS tree
; necht strom je (vrchol levy pravy)
(define (bvs-insert x tree)
  (if (null? tree)
      `(,x ,null ,null)
      (let ([node (car tree)])
           (cond
             ( (< x node) (cons node (cons (bvs-insert x (cadr tree)) (cons (caddr tree) null))) )
             ( (> x node) (cons node (cons (cadr tree) (cons (bvs-insert x (caddr tree)) null))) )
             ( #T tree )
           )
      )
  )
)

; bvs-create(list) - vytvořit BVS z prvků v poli list
(define (bvs-create lst)
  (if (null? lst)
      null
      (bvs-insert (car lst) (bvs-create (cdr lst)))
  )
)

; append lists
(define (my-append left right)
  (cond
    ( (and (null? left) (null? right)) null )
    ( (null? left) (cons (car right) (my-append left (cdr right))) )
    ( #T (cons (car left) (my-append (cdr left) right)) )
  )
)

; bvs-inorder(tree)
(define (bvs-inorder tree)
  (if (null? tree)
      null
      (my-append (bvs-inorder (cadr tree)) (cons (car tree) (bvs-inorder (caddr tree))))
  )
)

; treesort(list)
(define (treesort lst)
  (bvs-inorder (bvs-create lst))
)