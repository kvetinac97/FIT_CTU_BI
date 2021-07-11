package composite

// === COMPOSITE PATTERN
// Nevýhoda: jakmile změníme Expr, rozbije se úplně vše

const val PRIORITY_LOW  = 0
const val PRIORITY_HIGH = 1
const val PRIORITY_TOP  = 2

enum class Operation (val char: Char, val priority: Int = PRIORITY_LOW) {
    PLUS ('+'), MINUS ('-'), MLT ('*', PRIORITY_HIGH);
    override fun toString() = char.toString()
}

interface Expr {
    val value: Int
    val priority: Int
}

data class ConstantExpr (override val value: Int) : Expr {
    override val priority = PRIORITY_TOP
    override fun toString() = value.toString()
}

data class BinaryExpr (val operation: Operation, val left: Expr, val right: Expr) : Expr {
    // Initialization of property in constructor
    override val value = when (operation) {
        Operation.PLUS -> left.value + right.value
        Operation.MINUS -> left.value - right.value
        Operation.MLT -> left.value * right.value
    }
    override val priority = operation.priority
    override fun toString() : String {
        val l = if (left.priority  < priority) left.enclose()  else left.toString()
        val r = if (right.priority < priority) right.enclose() else right.toString()
        return "$l ${operation.char} $r"
    }

    companion object {
        // Extension function are static !!!
        fun Any.enclose() = "($this)"
    }
}

fun main () {
    println(ConstantExpr(128))

    val e1 = BinaryExpr(Operation.PLUS, ConstantExpr(5), ConstantExpr(3))
    val e2 = BinaryExpr(Operation.MLT , e1, ConstantExpr(4))

    println("$e2 = ${e2.value}")
}