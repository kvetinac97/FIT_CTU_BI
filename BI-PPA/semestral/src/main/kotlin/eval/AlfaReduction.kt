package eval

import Expression
import Expression.*
import cdr

fun replaceLast (original: String, char: Char) = original.dropLast(1) + char

tailrec fun firstUnbound (replace: String, expression: Expression, against: Expression, callCnt: Int = 0) : String =
    when {
        (replace !in against.unbound && replace !in expression.unbound) -> replace
        (replace.last() == 'z') -> firstUnbound(replace + "a", expression, against, callCnt + 1)
        else -> firstUnbound(replaceLast(replace, replace.last().inc()), expression, against, callCnt + 1)
    }

fun reduceAlfa (expression: LFunction, with: Expression) : Pair<LFunction, Boolean>
    = if (expression.args.first() in with.unbound)
        firstUnbound("a", expression, with).let { variableReplace ->
            LFunction(setOf(variableReplace) + expression.args.cdr(), rename(expression.expression, expression.args.first(), variableReplace))
        } to true
      else expression to false

fun rename (expression: Expression, variable: String, with: String) : Expression
    = when(expression) {
        is Variable -> if (expression.name == variable) Variable(with) else expression
        is LFunction -> if (expression.unbound.isNotEmpty()) LFunction(expression.args, rename(expression.expression, variable, with)) else expression
        is Application -> Application(rename(expression.first, variable, with), rename(expression.second, variable, with))
    }

tailrec fun renameAll (expression: Expression, variables: Set<String>, withs: Set<String>) : Expression =
    if (variables.size <= 1) expression
    else renameAll(rename(expression, variables.first(), withs.first()), variables.cdr(), withs.cdr())
