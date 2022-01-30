import Expression.*
import eval.renameAll

// Lambda vyraz is a variable, lambda function or application
sealed class Expression {
    abstract val unbound: Set<String>
    open fun toStringA() = toString()
    data class Variable    (val name: String) : Expression() {
        override val unbound = setOf(name)
        override fun toString() = name
    }
    data class LFunction (val args: Set<String>, val expression: Expression, val name: String? = null,
                         override val unbound: Set<String> = expression.unbound - args) : Expression() {
        override fun toString() = name ?: "(λ${args.joinToString("")}. $expression)"
    }
    data class Application (val first: Expression, val second: Expression) : Expression() {
        override val unbound = first.unbound + second.unbound
        override fun toString() = "(${toStringA()})"
        override fun toStringA() = "${first.toStringA()} $second"
    }
}

// Helper function to create lambda functions (with more variables) and named lambda functions
fun lambda (arg: String, expression: (Variable) -> Expression) = LFunction(setOf(arg), expression(Variable(arg)))
fun lambda (args: Set<String>, expression: (List<Variable>) -> Expression) = LFunction(args, expression(args.map { arg -> Variable(arg) }))
fun nlambda (name: String, args: Set<String>, expression: (List<Variable>) -> Expression) = LFunction(args, expression(args.map { arg -> Variable(arg) }), name)
fun nlambda (name: String, arg: String, expression: (Variable) -> Expression) = LFunction(setOf(arg), expression(Variable(arg)), name)

// Helper function for infix application creation
infix fun Expression.ap (other: Expression) = Application(this, other)

// Helper function for variable creation
fun v (name: String) = Variable(name)

// Helper extension function for Kotlin Set for Racket-like functionality
fun <T> Set<T>.cdr () = this - first()

// Helper function for comparing expressions
fun compareExpr (l: Expression, r: Expression) : Boolean =
    when (l) {
        is Variable -> l == r // classic compare
        is LFunction -> if (r is LFunction) {
            compareExpr(l.expression, renameAll(r.expression, r.args, l.args)) // ignore var names
        } else false
        is Application -> if (r is Application) {
            compareExpr(l.first, r.first) && compareExpr(l.second, r.second)
        } else false
    }

// Function tries to minify Church numerals as "named" lambda functions
tailrec fun minifyNumber (expression: Expression, original: LFunction, s: String, z: String, num: Int = 0) : Pair<Expression, Boolean> =
    when (expression) {
        is Variable -> {
            if (expression.name == z) LFunction(original.args, original.expression, num.toString()) to true
            else expression to false
        }
        is LFunction -> expression to false
        is Application -> {
            if (expression.first is Variable && expression.first.name == s)
                minifyNumber(expression.second, original, s, z, num + 1)
            else expression to false
        }
    }

// Minimalizations (unifying lambda functions into one with more variables...)
fun minify (expression: Expression, numbers: Boolean = false)  : Expression
    = when (expression) {
        is Variable -> expression
        is LFunction -> {
            if (expression.expression is LFunction) minify(LFunction (expression.args + expression.expression.args, expression.expression.expression), numbers)
            else if (expression.args.size == 2 && numbers) minifyNumber(expression.expression, expression, expression.args.first(), expression.args.cdr().first()).let { (expr, minified) ->
                if (minified) expr
                else LFunction(expression.args, minify(expression.expression, numbers), expression.name)
            }
            else LFunction(expression.args, minify(expression.expression, numbers), expression.name)
        }
        is Application -> Application(minify(expression.first, numbers), minify(expression.second, numbers))
    }