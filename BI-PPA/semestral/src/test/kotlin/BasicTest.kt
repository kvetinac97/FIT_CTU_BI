import eval.reduce

fun compare (l1: Expression, l2: Expression) = minify(reduce(l1), true).let { l3 ->
    println("$l1 reduced to $l3")
    println("$l3 " + (if (compareExpr(l2, l3)) "==" else "!=") + " $l2")
    println()
}

fun main () {
    // ((λx.xy) z) -> (z y)
    compare (lambda("x") { x ->
        x ap v("y")
    } ap v("z"), v("z") ap v("y"))

    // (λx.xoj) ((λy.(λz.zh)y)a) -> ahoj
    compare (lambda("x") { x ->
        x ap v("o") ap v("j")
    } ap (lambda("y") { y ->
        lambda("z") { z ->
            z ap v("h")
        } ap y
    } ap v("a")), v("a") ap v("h") ap v("o") ap v("j"))

    // (λx. (λy.y y) x) -> (λy.y y)
    compare (lambda("x") { x ->
        lambda("y") { y ->
            y ap y
        } ap x
    }, lambda("y") { y ->
        y ap y
    })

    // (λxy. (x y)) (a z) -> (a z)
    compare (lambda(setOf("x","y")) { (x,y) -> x ap y
    } ap (v("a") ap v("z")), v("a") ap v("z"))

    // (λxy. (x y)) (a y) -> (a y) !!!
    compare (lambda(setOf("x","y")) { (x,y) -> x ap y
    } ap (v("a") ap v("y")), (v("a") ap v("y")))

    // ((λxy. x) y z) -> y
    compare( lambda(setOf("x","y")) { (x,y) -> x } ap v("y") ap v("z"), v("y") )

    // (λab. a) c ( (λd.e) d)
    compare(lambda(setOf("a","b")) { (a,b) -> a } ap v("c") ap (lambda("d") { d -> v("e") } ap v("d")), v("c"))

    // 1 + 2 = 3
    compare(MLT ap num(3) ap num(4), num(12))

    // (λxya. (x y)) (a y) z -> (λa1 . a y z)
    compare(lambda(setOf("x","y","a")) { (x,y,a) ->
        x ap y
    } ap (v("a") ap v("y")) ap v("z"), lambda("b") { b -> v("a") ap v("y") ap v("z") })

    compare(lambda("x") { x ->
        lambda("y") { y ->
            x ap (y ap y)
        }
    }, lambda(setOf("x", "y")) { (x,y) -> x ap (y ap y) })

    compare (lambda(setOf("x","y")) { (x,y) -> x ap y
    } ap (v("a") ap v("y")), (v("a") ap v("y")))

    compare (lambda(setOf("x","y")) { (x,y) ->
        v("z") ap y
    }, lambda("x") { x ->
        v("z")
    })

    compare(FAC ap ZERO, num(1))
    compare(FAC ap ONE, num(1))
    compare(FAC ap num(2), num(2))
    compare(FAC ap num(3), num(6))
    compare(FAC ap num(4), num(24))
    compare(FAC ap num(5), num(120))
}