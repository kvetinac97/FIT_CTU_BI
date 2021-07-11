import kotlin.coroutines.coroutineContext

// Rozšíření již existujících objektů o nové funkce
fun String.prependKot() = "[BI-KOT] ".plus(this)

// Lze rozšířit také nullable typy, pozor na null
fun Any?.bomb() = this?.toString() ?: "nothing"

// Lze rozšířit také property
val String.lengthSquared get () = length * length

/* odpovídalo by
extension String {
    func prependKot () -> String {
        return "[BI-KOT] " + self
    }
}*/

// Třídy jsou klasicky final, pokud chceme dědění, musí být na open
open class C {
    open fun foo() = "foo"
}
class D: C() {
    override fun foo() = "override"
}

fun C.bar() = "not"
fun D.bar() = "over"

// lze importovat funkci zvláště
// import packagename.funcname as something

fun main () {
    val c: C = D()
    println("${c.foo()} ${c.bar()} ${"hola".lengthSquared}")
    println("Something interesting".prependKot())

    val map = mapOf("key" to "love", "test" to "awesome")
    println(map)
}
