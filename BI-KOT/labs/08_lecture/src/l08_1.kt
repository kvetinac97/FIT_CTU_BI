
// Templated class
// note: as U has no set type, it is explicit to U: Any?
data class TupleInt<T: Any, U> (val first: T, val second: U)

// We can template extension parameters (or functions)
val <T> List<T>.test
get() = size - 1

// Reified = make type more concrete = refer to actual type
inline fun <reified T> sameAs (value: Any) = value is T

fun main () {
    val test = TupleInt("Test", null) // test: Tuple<Int, Any?>
    // failes to compile - val error: Tuple

    println(test)
    println(listOf(3, 22, 1).test)

    println(sameAs<Int>(5))
    println(sameAs<Double>(5))
}