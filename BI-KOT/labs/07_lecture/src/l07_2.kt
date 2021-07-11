package l07_2

data class Point (val x: Int, val y: Int) {
    // Umožní volání p1.plus(p2) nebo p1 + p2
    operator fun plus (other: Point) = Point(x + other.x, y + other.y)
    // Umožní volání p1.times(p2), p1 * p2 nebo p1 times p2
    infix operator fun times (other: Double) = Point((x * other).toInt(), (y * other).toInt())
}

class CustomEquals (val value: Int) : Comparable<CustomEquals> {
    override fun compareTo(other: CustomEquals) = value.compareTo(other.value)
}

// Extension funkce, abychom mohli volat i 3 * Point(2,2)
operator fun Double.times (other: Point) = other * this

fun main () {
    // Overloading arithmetic operations
    val point = Point(1, 1)
    val point2 = point + Point(2, 3)

    println("$point $point2")
    println(point times 3.0)
    println(4.2 * point2)

    // Overloading compare
    val ceq = CustomEquals(5)
    val ceq2 = CustomEquals(6)
    val ceq3 = CustomEquals(5)
    println("${ceq > ceq2} ${ceq <= ceq3} ${ceq < ceq3}")
}