// Funkce jsou deklarovány klasicky
private fun test (parameter: String?) : Int {
    println(parameter ?: "Default value")
    return parameter?.length ?: 1
}

// Jednořádkové funkce se umí zkrátit
fun test(parameter: Int = 3) = parameter * 2

// Unit = funkce nevracející žádný datový typ

// vararg = jakýkoliv vhodný typ
fun asList(vararg ts: Int): List<Int> {
    val result = ArrayList<Int>()
    for (t in ts)
        result.add(t)
    return result
}

class InfixShowcase(val l: Int) {
    infix fun test (left: InfixShowcase) = InfixShowcase(left.l * l)
}

fun main () {
    // Umožňujeme i jmenné parametry
    println(test(test(parameter = null)))
    println(test(test("Hello")))
    println(test())


    val a = intArrayOf(3, 4)
    // hvězdička odpovídá spread operátoru
    println(asList(1, 2, *a, 5))

    // můžeme použít i Infix
    println((InfixShowcase(5) test InfixShowcase(3)).l)
}