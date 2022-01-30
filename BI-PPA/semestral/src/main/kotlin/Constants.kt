import eval.reduce

// Konstanty a funkce

// Logicke hodnoty

// TRUE = (λtf. t)
val TRUE  = nlambda("#T", setOf("t", "f")) { (t,f) -> t }

// FALSE = (λtf. f)
val FALSE = nlambda("#F", setOf("t", "f")) { (t,f) -> f }

// AND = (λx. (λy. x y FALSE))
val AND = nlambda("AND", setOf("x", "y")) { (x,y) ->
    x ap y ap FALSE
}

// OR = (λx. (λy. x TRUE y))
val OR = nlambda("OR", setOf("x", "y")) { (x,y) ->
    x ap TRUE ap y
}

// Numericke hodnoty

val ZERO = nlambda("0", setOf("s", "z")) { (s,z) -> z }

// Eta reduced
val ONE = nlambda("1", setOf("s", "z")) { (s,z) -> s ap z }

val INC = nlambda("INC",setOf("x", "s", "z")) { (x,s,z) ->
    s ap (x ap s ap z)
}

tailrec fun num(x: Int, original: Int = x, acc: Expression = ZERO) : Expression =
    if (x == 0) minify(reduce(acc)).let { f -> if (f is Expression.LFunction) Expression.LFunction(f.args, f.expression, original.toString(), f.unbound) else f }
    else num(x - 1, original,INC ap acc)

// (λx. λy. λs. λz. x s (y s z))
val PLUS = nlambda("+", setOf("x", "y", "s", "z")) { (x,y,s,z) ->
    x ap s ap (y ap s ap z)
}

// (λ xsz . x (λ f g . g (f s)) (λ g . z) (λ u . u))
val PRED = nlambda("PRED", setOf("x", "s", "z")) { (x,s,z) ->
    (x ap lambda("f") { f ->
        lambda("g") { g ->
            g ap (f ap s)
        }
    }) ap lambda("g") { g -> z } ap lambda("u") { u -> u }
}

// (λmn . (n PRED) m)
val MINUS = nlambda("-", setOf("m", "n")) { (m, n) ->
    (n ap PRED) ap m
}

// (λx. λy. λs. x (y s)
val MLT = nlambda("*", setOf("x","y","s")) { (x,y,s) ->
    x ap (y ap s)
}

// (λf. (λx.f(xx))(λx.f(xx)))
val Y = nlambda("Y", "f") { f ->
    lambda("x") { x ->
        f ap (x ap x)
    } ap lambda("x") { x ->
        f ap (x ap x)
    }
}

// (λ n . n (λ x . (λ t f . f)) (λ t f . t))
val IS_ZERO = nlambda("Zero", "n") { n ->
    n ap lambda("x") { x -> FALSE } ap TRUE
}

// Y (λf. λn. Zero n 1 (* n (f (-  n  1))))

val INNER_FAC = nlambda("FAC", setOf("f","n")) { (f,n) ->
    IS_ZERO ap n ap ONE ap (MLT ap n ap (f ap (MINUS ap n ap ONE)))
}

val FAC = Y ap INNER_FAC