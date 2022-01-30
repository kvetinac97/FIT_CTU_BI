package eval

import Expression
import Expression.*

fun reduceEta (expression: LFunction, applicative: Boolean) : Pair<Expression, Boolean> =
    // Perform eta reduction
    if (expression.expression is Application && expression.expression.second is Variable
            && expression.expression.second.name == expression.args.first()
            && expression.args.first() !in expression.expression.first.unbound)
        (removeFirstParam(expression, expression.expression.first) to true)
    // Continue
    else reduceStep(expression.expression, applicative).let { (expr, reduced) ->
        LFunction(expression.args, expr) to reduced
    }
