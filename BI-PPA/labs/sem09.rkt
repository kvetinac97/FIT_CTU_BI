#lang racket
(require compatibility/defmacro)
(require racket/match (for-syntax racket/match))

; V LISPu se kód evaluuje, než ho funkci pošlu
(define (foo a b) (+ a b))

(define-macro (foo-macro a b) ``(+ ,,a ,,b))

; (foo (+ 1 2) 3) vrátí 6
; (foo-macro (+ 1 2) 3) vrátí seznam '(+ a b)

; Chceme napsat if bez podminky
(define-macro (if-yes cond then)
  (list `if cond then ``())
)

; Muzeme udelat dvema zpusoby
(define-macro (if-no cond then)
  `(if ,cond `() ,then)
)

; Druha verze je chytrejsi pro dlouhe kody

(if-yes (equal? (+ 1 1) 2) 1)
(if-no (equal? (+ 1 1) 2) 1)

; (my-let name value body) -> ((lambda (name) body) value)

(define-macro (my-let name value body)
  `((lambda (,name) ,body) ,value)
)

; Multi - variable macro
; (my-let2 ((name1 value1) (name2 value2)) body)

(define-macro (define-dual nargs body)
  `(begin
     (define ,nargs ,body)
     (begin-for-syntax (define ,nargs ,body))
   )
)
  
(define-dual (decl-names decls)
  (map (lambda (x) (car x)) decls)
)

(define-dual (decl-values decls)
  (map (lambda (x) (cadr x)) decls)
)

(define-macro (my-let2 decls body)
  `((lambda ,(decl-names decls) ,body) ,@(decl-values decls))
)

; @ odpovídá splice = (1 2 3 (4 5)) udělá (1 2 3 4 5)

(my-let x 1 (+ x 2))
(my-let2 ((x 1) (y 2)) (+ x y))

; Co když chceme vlastní for-loop
; potřebujeme vlastní sekvenci a foreach

(define-dual (my-seq from to)
  (if (equal? from to)
      (cons from null)
      (cons from (my-seq (+ from 1) to))
  )
)

(my-seq 1 5)

(define/match (foreach list f)
  [(`() _) `()] ; prázdný seznam = je mi to jedno
  [((list-rest i rest) f) (begin (f i) (foreach rest f))] ; begin = nechci udělat nic, cons = udělal by map
)

(define-macro (for exprs body)
  (match exprs
    [(list name start end) ; Kontrola, že ty proměnné jsou a přidá je to
      `(foreach (my-seq ,start ,end) (lambda (,name) ,body))
    ] 
    [_ (error "Invalid format")]
  )
)

(foreach `(1 2 3 4 5) (lambda (x) (+ x 1)))
(for (i 3 7) (* i i))
