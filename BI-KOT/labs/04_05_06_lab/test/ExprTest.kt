import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import dsl.*
import patterns.nullable.optimnull
import patterns.optim
import visitor.*

// Helper function to get expression text and value
fun Expr.postfix () : Pair<String, Int> {
    val visitor = PostfixVisitor()
    accept(visitor)
    return Pair(visitor.result.toString().trim(), visitor.stack.pop())
}

class ExprTest : StringSpec ({
    "Basic test" {
        // 42 = 42
        expr { num(42) }.postfix() shouldBe Pair("42", 42)

        // +1 + 1 = 2
        expr {
            pls {
                uplus { num(1) }
                num(1)
            }
        }.postfix() shouldBe Pair("1 + 1 +", 2)

        // 0 - -12 = 12
        expr {
            mns {
                num(0)
                uminus { num(12) }
            }
        }.postfix() shouldBe Pair("0 12 - -", 12)

        // -3 * --+5 = -15
        expr {
            mlt {
                uminus { num(3) }
                uminus { uminus { uplus { num(5) } } }
            }
        }.postfix() shouldBe Pair("3 - 5 + - - *", -15)
    }
    "More nested operations test" {
        // ( -5 + 3 ) * 4 = -8
        expr {
            mlt {
                pls {
                    uminus { num(5) }
                    num (3)
                }
                num(4)
            }
        }.postfix() shouldBe Pair("5 - 3 + 4 *", -8)

        // // 1 * (0 + ((+5 + 0) * --1)) = 5
        expr {
            mlt {
                num(1)
                pls {
                    num(0)
                    mlt {
                        pls {
                            pls {
                                uplus { num(5) }
                                num(0)
                            }
                            num(0)
                        }
                        uminus { uminus { num(1) } }
                    }
                }
            }
        }.postfix() shouldBe Pair("1 0 5 + 0 + 0 + 1 - - * + *", 5)
    }
    "Optimization test" {
        val e1 = expr {
            mlt {
                num(1)
                pls {
                    num(0)
                    mlt {
                        pls {
                            pls {
                                uplus { num(5) }
                                num(0)
                            }
                            num(0)
                        }
                        uminus { uminus { num(1) } }
                    }
                }
            }
        }
        e1.optim() shouldBe e1.optimnull()
        e1.optimnull() shouldBe expr { num(5) }

        val e2 = expr {
            mns {
                mlt {
                    num(0)
                    add(e1)
                }
                mlt {
                    add(e1)
                    num(0)
                }
            }
        }
        e2.optim() shouldBe e2.optimnull()
        e2.optimnull() shouldBe expr { num(0) }
    }
})