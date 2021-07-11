// You can add new properties to object
// not field !!!
val String.blahblah
get() = this.lengthSquared

// Setter pouze pro kladna cisla
var counter = 0
set (value) {
    if (value >= 0)
        field = value
}

class Lazy {
    lateinit var lazy: String

    fun setup() {
        lazy = "Test"
    }
}

class Point(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}

fun main () {
    // Naše definovaná proměnná
    println("lol".blahblah)

    // Ukázka funkce setteru
    println(counter)
    counter += 10
    counter -= 20
    println(counter)

    // Lateinit
    val l = Lazy()
    l.setup()
    println(l.lazy)

    // Destructing declarations
    val (x, y) = Point(12, 24)
    println("Point: [$x,$y]")

    // It can be used also in map
    for ( (key, value) in mapOf(Pair(15, 13), Pair(7, 2)) )
        println("$key -> $value")

}