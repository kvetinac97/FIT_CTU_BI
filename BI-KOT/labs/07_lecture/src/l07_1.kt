package l07_1

fun main () {
    // Null + smart cast
    val x: Int? = null
    val y: Int? = 3
    val z = if ( x != null && y != null ) x + y else 1 + 2
    println("Z is $z")
    // Arithmetic thanks to extension functions
    println("Extension functions allow us to do ${5 + 1L} = 5 + 1L")

    // Error
    val v = y ?: error("Toto je špatně")
    println("Value was $v")

    // String split is smart now
    println("Split is ${"He.ll.o!".split(".")}")
}