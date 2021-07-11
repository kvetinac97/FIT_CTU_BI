
operator fun Int.plus (other: Any?) = "$this$other"

fun cube (x: Int) = x * x * x

fun takeInt (method: ((Int) -> Int)?) = method

fun main () {
    // Lambda can act as a value
    val sqr = takeInt { x: Int -> x * x} ?: return
    val sqr2: (Int) -> Int = { it * it } // it is placeholder
    println(sqr(5) + " " + sqr2(5) + " " + cube(5))

    val function = String::length // member reference
    println(function.get("test"))
}