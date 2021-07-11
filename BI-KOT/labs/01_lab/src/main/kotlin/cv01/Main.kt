package cv01

fun name () = "Antone Pavlovici"
data class Person (val name: String)

fun main () {
    val p1 = Person("Tom");
    println("Neni to malo, ${name()} a ${p1}?")
}
