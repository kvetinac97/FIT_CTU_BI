package patterns

import visitor.*

interface ExprPattern {
    fun match (e: Expr) : Boolean
}

object UniversalPattern : ExprPattern {
    override fun match (e: Expr) = true
}

open class NumericPattern (private val value: Int) : ExprPattern {
    override fun match (e: Expr) : Boolean {
        val visitor = PostfixVisitor()
        e.accept(visitor)
        return visitor.stack.pop() == value
    }
}

object ZeroPattern : NumericPattern(0)
object OnePattern  : NumericPattern(1)

open class UnaryPattern (private val operations: Array<Operation> = Operation.values(),
                         private val pattern: ExprPattern = UniversalPattern) : ExprPattern {
    override fun match (e: Expr) = e is UnaryExpr && e.operation in operations && pattern.match(e.operand)
}

object UnaryPlusPattern : UnaryPattern(arrayOf(Operation.PLUS))
open class UnaryMinusPattern (pattern: ExprPattern = UniversalPattern) : UnaryPattern(arrayOf(Operation.MINUS), pattern)
object UnaryDoubleMinusPattern : UnaryMinusPattern(UnaryMinusPattern())

open class BinaryPattern (private val operations: Array<Operation> = Operation.values(),
                          private val leftPattern: ExprPattern = UniversalPattern,
                          private val rightPattern: ExprPattern = UniversalPattern) : ExprPattern {
    override fun match (e: Expr)
            = e is BinaryExpr && e.operation in operations && leftPattern.match(e.left) && rightPattern.match(e.right)
}

// 0 + x = x
object BinaryPlusZeroPattern1      : BinaryPattern (arrayOf(Operation.PLUS), ZeroPattern)
// x + 0 = x - 0 = x
object BinaryPlusMinusZeroPattern2 : BinaryPattern (arrayOf(Operation.PLUS, Operation.MINUS), UniversalPattern, ZeroPattern)
// 1 * x = x * 1 = x
object BinaryMultiplyOnePattern1   : BinaryPattern (arrayOf(Operation.MLT), OnePattern)
object BinaryMultiplyOnePattern2   : BinaryPattern (arrayOf(Operation.MLT), UniversalPattern, OnePattern)
// 0 * x = x * 0 = 0
object MultiplyByZeroPattern1      : BinaryPattern (arrayOf(Operation.MLT), ZeroPattern)
object MultiplyByZeroPattern2      : BinaryPattern (arrayOf(Operation.MLT), UniversalPattern, ZeroPattern)

fun Expr.optim() : Expr {
    return when {
        UnaryPlusPattern.match(this)        -> (this as UnaryExpr).operand.optim()
        UnaryDoubleMinusPattern.match(this) -> ((this as UnaryExpr).operand as UnaryExpr).operand.optim()
        BinaryPlusZeroPattern1.match(this)      -> (this as BinaryExpr).right.optim()
        BinaryPlusMinusZeroPattern2.match(this) -> (this as BinaryExpr).left.optim()
        BinaryMultiplyOnePattern1.match(this)   -> (this as BinaryExpr).right.optim()
        BinaryMultiplyOnePattern2.match(this)   -> (this as BinaryExpr).left.optim()
        MultiplyByZeroPattern1.match(this) -> ZERO
        MultiplyByZeroPattern2.match(this) -> ZERO
        else -> this
    }
}

fun main() {
    // 1 * (0 + ((+5 + 0) * --1)) = 5
    val e0 = BinaryExpr(Operation.PLUS, UnaryExpr(Operation.PLUS, ConstantExpr(5)), ZERO) // 0 + +5
    val e1 = UnaryExpr(Operation.MINUS, UnaryExpr(Operation.MINUS, ONE)) // --1 = 1
    val e2 = BinaryExpr(Operation.PLUS, ZERO, BinaryExpr(Operation.MLT, e0, e1))
    val ue = BinaryExpr(Operation.MLT, ONE, e2)
    println("$ue is optimized to ${ue.optim()}")

    // 0 * ... - ... * 0 = 0
    val u2 = BinaryExpr(Operation.MINUS, BinaryExpr(Operation.MLT, ZERO, ue), BinaryExpr(Operation.MLT, ue, ZERO))
    println("$u2 is optimized to ${u2.optim()}")
}