package dsl

import visitor.*
import java.util.*

// Helper function to minimize code
fun <T, R : T> Queue<T>.addReturn (t: R) : R {
    add(t)
    return t
}

// Syntax shortcut for creating linked list
fun expr (block: Queue<Expr>.() -> Unit) : Expr = LinkedList<Expr>().apply(block).element()

// Each function has to create own node
fun Queue<Expr>.num (num: Int) = addReturn(ConstantExpr(num))

fun Queue<Expr>.unary  (op: Operation, block: Queue<Expr>.() -> Unit)
    = addReturn(UnaryExpr(op, expr(block)))

// Syntax shortcut for unary functions
fun Queue<Expr>.uplus  (block: Queue<Expr>.() -> Unit) = unary(Operation.PLUS , block)
fun Queue<Expr>.uminus (block: Queue<Expr>.() -> Unit) = unary(Operation.MINUS, block)

fun Queue<Expr>.binary (op: Operation, block: Queue<Expr>.() -> Unit) : BinaryExpr {
    // helper variable for queue
    val q = LinkedList<Expr>().apply(block).apply { assert(size == 2) }
    return addReturn(BinaryExpr(op, q.pop(), q.pop()))
}

// Syntax shortcut for binary functions
fun Queue<Expr>.pls (block: Queue<Expr>.() -> Unit) = binary(Operation.PLUS , block)
fun Queue<Expr>.mns (block: Queue<Expr>.() -> Unit) = binary(Operation.MINUS, block)
fun Queue<Expr>.mlt (block: Queue<Expr>.() -> Unit) = binary(Operation.MLT  , block)

fun main () {
    val e = expr {
        mlt {
            pls {
                mns {
                    uminus { num(1) }
                    uplus { num(2) }
                }
                num(3)
            }
            mns {
                num(0)
                pls {
                    num(3)
                    num(12)
                }
            }
        }
    }
    println("$e")
}