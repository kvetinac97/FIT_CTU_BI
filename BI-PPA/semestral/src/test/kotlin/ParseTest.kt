import parse.printParse

fun main () {
    // === BASIC TEST ===
    printParse("((λx.((x o) j)) ((λy.((λz.(z h)) y)) a))")
    printParse("(λxy.(x y))")
    // ==================

    // === SUBTRACTION TEST ===
    printParse("((λx. ((λy. (- x y)) 2)) 5)")
    printParse("((λx. ((λy. (- x y)) 5)) 2)")
    printParse("(((λxy. (- x y)) 5) 2)")
    // ========================

    // === ADVANCED TEST ===
    printParse("((λx. (* (+ 3 x) (- x 4))) 5)")
    printParse("((λx. ((λy. (* (+ y z) (- x 2))) 5)) 4)")
    printParse("(((λz. (z z)) (λz. z)) (λz. (z q)))")
    printParse("(((λf. (λx. (f (f x)) ) ) (λ y. (* y y))) 2)")
    printParse("(((λfxy. ((f x) y)) (λga. (((g g) g) a))) (λhb. (h b)))")
    // =====================

    // === APPLICATIVE VS NORMAL TEST ===
    printParse("((λx. (+ x x)) ((λp. (+ p 4)) 3))")
    printParse("((λx. (+ x x)) ((λp. (+ p 4)) 3))", true)

    printParse("( | (& F T) (& T T) )", true)
    printParse("( | (& F T) (& T T) )", false)
    // ==================================

    // === IF TEST ===
    printParse("((T 0) 1)")
    // ===============
}
