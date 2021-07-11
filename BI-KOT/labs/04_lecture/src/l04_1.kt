// Data class
data class F (val test: String, val num: Int)

// Enum class
enum class Color (val rgb: Int) {
    RED   (0xFF0000) {
        override fun pointless() = 128
    },
    GREEN (0x00FF00) {
        override fun pointless() = 24
    },
    BLUE  (0x0000FF) {
        override fun pointless() = 30
    };
    abstract fun pointless() : Int
}

// inline a reified umožní použít typ parametru
inline fun <reified T : Enum<T>> printAll () = print(enumValues<T>().joinToString { it.name })

fun main () {
    // Toto by bez data class neslo
    val f = F("jedna", 25)
    val g = F("jedna", 25)
    println("$f | ${f == g}")

    // Kopie (funkce copy)
    val h = g.copy(test="dva")

    // Destructing deklarace (componentN funkce)
    val (test, num) = h
    println("Test: $test a cislo: $num")

    // Enumy
    val color = Color.GREEN
    for ( col in Color.values() )
        println("Color $col #${col.rgb} [${col.pointless()}] with ordinal ${col.ordinal}, order ${col < color}")
    printAll<Color>()
}