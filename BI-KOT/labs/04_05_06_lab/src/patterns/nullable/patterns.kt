package patterns.nullable

import visitor.*

interface ExprPattern {
    fun match (e: Expr) : Expr?
}

object UniversalPattern : ExprPattern {
    override fun match (e: Expr) = e
}

open class NumericPattern (private val value: Int) : ExprPattern {
    override fun match (e: Expr) : Expr? {
        val visitor = PostfixVisitor()
        e.accept(visitor)
        return if (visitor.stack.pop() == value) e else null
    }
}

object ZeroPattern : NumericPattern(0)
object OnePattern  : NumericPattern(1)

open class UnaryPattern (private val operations: Array<Operation> = Operation.values(),
                         private val pattern: ExprPattern = UniversalPattern) : ExprPattern {
    override fun match (e: Expr) : UnaryExpr? =
        if (e is UnaryExpr && e.operation in operations && pattern.match(e.operand) != null) e
        else null
}

object UnaryPlusPattern : UnaryPattern(arrayOf(Operation.PLUS))
open class UnaryMinusPattern (pattern: ExprPattern = UniversalPattern) : UnaryPattern(arrayOf(Operation.MINUS), pattern)
object UnaryDoubleMinusPattern : UnaryMinusPattern(UnaryMinusPattern())

open class BinaryPattern (private val operations: Array<Operation> = Operation.values(),
                          private val leftPattern: ExprPattern = UniversalPattern,
                          private val rightPattern: ExprPattern = UniversalPattern) : ExprPattern {
    override fun match (e: Expr) : BinaryExpr? =
        if (e is BinaryExpr && e.operation in operations && leftPattern.match(e.left) != null && rightPattern.match(e.right) != null) e
        else null
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

fun Expr.optimnull() : Expr = UnaryPlusPattern.match(this)?.operand?.optimnull()
    ?: UnaryDoubleMinusPattern.match(this)?.operand?.let { it as UnaryExpr }?.operand?.optimnull()
    ?: BinaryPlusZeroPattern1.match(this)?.right?.optimnull()
    ?: BinaryMultiplyOnePattern1.match(this)?.right?.optimnull()
    ?: BinaryPlusMinusZeroPattern2.match(this)?.left?.optimnull()
    ?: BinaryMultiplyOnePattern2.match(this)?.left?.optimnull()
    ?: (if (MultiplyByZeroPattern1.match(this) != null ||
            MultiplyByZeroPattern2.match(this) != null) ZERO else null )
    ?: this

fun main() {
    // 1 * (0 + ((+5 + 0) * --1)) = 5
    val e0 = BinaryExpr(Operation.PLUS, UnaryExpr(Operation.PLUS, ConstantExpr(5)), ZERO) // 0 + +5
    val e1 = UnaryExpr(Operation.MINUS, UnaryExpr(Operation.MINUS, ONE)) // --1 = 1
    val e2 = BinaryExpr(Operation.PLUS, ZERO, BinaryExpr(Operation.MLT, e0, e1))
    val ue = BinaryExpr(Operation.MLT, ONE, e2)
    println("$ue is optimized to ${ue.optimnull()}")

    // 0 * ... - ... * 0 = 0
    val u2 = BinaryExpr(Operation.MINUS, BinaryExpr(Operation.MLT, ZERO, ue), BinaryExpr(Operation.MLT, ue, ZERO))
    println("$u2 is optimized to ${u2.optimnull()}")
}