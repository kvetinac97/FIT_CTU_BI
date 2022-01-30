package eval

import Expression
import Expression.*
import cdr

fun reduceStep (expression: Expression, applicative: Boolean) : Pair<Expression, Boolean> =
    when (expression) {
        is Variable -> expression to false // nothing to evaluate
        is LFunction -> when (expression.name) {
            null -> {
                // lambda function with more variables expansion
                if (expression.args.size > 1)
                    (LFunction(setOf(expression.args.first()), LFunction(expression.args.cdr(), expression.expression)) to true)
                // eta conversion
                else reduceEta(expression, applicative)
            }
            // expansion
            else -> (LFunction(expression.args, expression.expression, unbound = expression.unbound) to true)
        }
        is Application -> reduceBeta(expression, applicative) // beta reduction
    }

tailrec fun reduceImpl (result: Pair<Expression, Boolean>, applicative: Boolean) : Expression =
    if (result.second) reduceImpl(reduceStep(result.first, applicative), applicative)
    else result.first

fun reduce (expression: Expression, applicative: Boolean = false) : Expression
    = reduceImpl(reduceStep(expression, applicative), applicative)

tailrec fun reducePrintStepImpl (result: Pair<Expression, Boolean>, applicative: Boolean, step: Int = 1) : Expression =
    if (result.second) reducePrintStepImpl(reduceStep(result.first, applicative)
        .apply { println("$step: ${result.first}") }, applicative, step + 1)
    else result.first

fun reducePrint (expression: Expression, applicative: Boolean = false) : Expression
        = reducePrintStepImpl(expression to true, applicative)