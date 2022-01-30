package eval

import Expression
import Expression.*
import cdr

fun removeFirstParam (fn: LFunction, inner: Expression) =
    (if (fn.args.size == 1) inner
    else LFunction(fn.args.cdr(), inner))


fun reduceBeta (expression: Application, applicative: Boolean) : Pair<Expression, Boolean>
    // Beta reduction can be performed
    = if (expression.first is LFunction) {
        // Lambda function with more variables expansion
        if (expression.first.args.size > 1)
            reduceBeta(Application(LFunction(setOf(expression.first.args.first()),
                LFunction(expression.first.args.cdr(), expression.first.expression)), expression.second), applicative)
        else
        // Check for alfa conversion first
        checkAlfa(expression.first.expression, expression.first.args.first(), expression.second).let { (alfaReduced, alfa) ->
            if (alfa) Application(LFunction(expression.first.args, alfaReduced), expression.second) to true
            // Applicative = first evaluate the parameter
            else if (applicative) {
                reduceStep(expression.second, applicative).let { (reduced, wasReduction) ->
                    if (wasReduction) Application(expression.first, reduced) to true
                    else reduceBeta(expression, false)
                }
            }
            // Can perform beta
            else removeFirstParam(expression.first, replace(expression.first.expression, expression.first.args.first(), expression.second)) to true
        }
    }
    else reduceStep(expression.first, applicative).let { (expr, reduced) ->
        // Reduction performed on left param
        if (reduced) Application(expr, expression.second) to true
        // Was not successful, try to reduce on right param
        else reduceStep(expression.second, applicative).let { (exp, reduce) ->
            Application(expression.first, exp) to reduce
        }
    }

fun checkAlfa (expression: Expression, variable: String, with: Expression) : Pair<Expression, Boolean>
    = when(expression) {
        is Variable -> expression to false
        is LFunction -> {
            if (expression.args.first() == variable || expression.unbound.isEmpty()) expression to false
            else reduceAlfa(expression, with).let { (reducedExpr, alfa) ->
                if (alfa) reducedExpr to true
                else checkAlfa(expression.expression, variable, with).let { (replacedExpr, alfaExpr) ->
                    if (alfaExpr) LFunction(expression.args, replacedExpr) to true
                    else expression to false
                }
            }
        }
        is Application -> checkAlfa(expression.first, variable, with).let { (replacedLeft, alfaLeft) ->
            if (alfaLeft) Application(replacedLeft, expression.second) to true
            else checkAlfa(expression.second, variable, with).let { (replacedRight, alfaRight) ->
                if (alfaRight) Application(replacedLeft, replacedRight) to true
                else Application(replacedLeft, replacedRight) to false
            }
        }
    }

fun replace (expression: Expression, variable: String, with: Expression) : Expression =
    if (expression.unbound.isEmpty()) expression
    else when(expression) {
        is Variable -> if (expression.name == variable) with else expression
        is LFunction -> {
            if (expression.args.first() == variable) expression
            else LFunction(expression.args, replace(expression.expression, variable, with))
        }
        is Application -> Application(replace(expression.first, variable, with), replace(expression.second, variable, with))
    }