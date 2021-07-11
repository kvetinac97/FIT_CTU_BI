class SoftShowcase (private val width: Int, private val height: Int) {
    val isSquare: Boolean
    get() = height == width // get is soft keyword
}

enum class TestEnum {
    BLUE, RED, WHITE, YELLOW; // here, mandatory comma

    // when == match like in PHP, smarter
    fun rgb () : Int = when(this) {
        BLUE, RED -> 25
        WHITE -> 13
        else -> 22
    }

    // when can work with bool
    fun ggb () : Int = when {
        this == BLUE || this == YELLOW -> 15
        else -> 0
    }
}

fun main () {
    // Smart cast (no need for ((Test) something).ggb()
    val something : Any = TestEnum.BLUE
    if (something is TestEnum)
        println("${something.ggb()} ${something.rgb()}")

    // Block = výraz, jehož hodnota je poslední řádek bloku
    val test = if (something is TestEnum) something.rgb() else 25
    println(test)

    // while by se NEMĚLY používat

    // ranges
    var rr: Any = 1..10 // range
    rr = 10 downTo 5 // progression
    rr = 10 downTo 3 step 3

    // operator in
    for (num in rr)
        print("$num ")
    println()

    println('a' in 'a'..'z')
    println(5 in 25..30)

    val map = mapOf(Pair(25, 30), Pair(11, 15))
    for ((f, s) in map)
        println("Map element $f => $s")

    // classical try and catch
}