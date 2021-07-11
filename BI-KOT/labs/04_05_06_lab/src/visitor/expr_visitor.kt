package visitor

import java.util.Stack

// === VISITOR PATTERN
// Navštěvuje jednotlivé uzly stromu
// na každém zavolá visit, uzel přijme visitora

enum class Operation (val char: Char) {
    PLUS ('+'), MINUS ('-'), MLT ('*');
    override fun toString() = char.toString()
}

interface ExprVisitor {
    fun visitConstant (node: ConstantExpr)
    fun visitUnary    (node: UnaryExpr   )
    fun visitBinary   (node: BinaryExpr  )
}

interface Expr {
    fun accept (visitor: ExprVisitor)
}

data class ConstantExpr (val value: Int) : Expr {
    override fun accept (visitor: ExprVisitor) = visitor.visitConstant(this)
    override fun toString() = value.toString()
}

val ZERO = ConstantExpr(0)
val ONE  = ConstantExpr(1)

data class UnaryExpr    (val operation: Operation, val operand: Expr) : Expr {
    override fun accept (visitor: ExprVisitor) = visitor.visitUnary(this)
    override fun toString() = "$operation$operand"
}

data class BinaryExpr   (val operation: Operation, val left: Expr, val right: Expr) : Expr {
    override fun accept (visitor: ExprVisitor) = visitor.visitBinary(this)
    override fun toString() = "$left $operation $right"
}

class PostfixVisitor : ExprVisitor {
    val stack  = Stack<Int>()
    val result = StringBuilder()

    override fun visitConstant (node: ConstantExpr) {
        stack.push(node.value) // Eval
        result.append(' ').append(node) // Print
    }
    override fun visitUnary    (node: UnaryExpr ) {
        // Eval + print
        node.operand.accept(this)

        // Eval
        val v = stack.pop()
        val res = when (node.operation) {
            Operation.PLUS  -> v
            Operation.MINUS -> -v
            Operation.MLT   -> throw IllegalArgumentException("Cannot use * as unary operator!")
        }

        stack.push(res)

        // Print
        result.append(' ').append(node.operation.char)
    }
    override fun visitBinary   (node: BinaryExpr) {
        // Eval + print
        node.left.accept(this)
        node.right.accept(this)

        // Eval
        val right  = stack.pop()
        val left   = stack.pop()

        val res = when (node.operation) {
            Operation.PLUS  -> left + right
            Operation.MINUS -> left - right
            Operation.MLT   -> left * right
        }

        stack.push(res)

        // Print
        result.append(' ').append(node.operation.char)
    }
}

fun main () {
    val visitor = PostfixVisitor()
    val e0 = ConstantExpr(5)
    val e1 = BinaryExpr(Operation.PLUS, UnaryExpr(Operation.MINUS, e0), ConstantExpr(3))
    val e2 = BinaryExpr(Operation.MLT , e1, ConstantExpr(4))

    e2.accept(visitor)
    println("${visitor.result} = ${visitor.stack.pop()}")
}
