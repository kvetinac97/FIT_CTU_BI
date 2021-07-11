import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface I {
    fun f ()
}

class Implementation : I {
    override fun f() = println("Wow")
}

// Delegation pattern
// zdedena funkce bude automaticky delegovana pres d
class Delegate (d: I) : I by d
// uvnitr tridy bude autogenerovano
// override fun f() { d.f() }

// Lze delegovat take properties a atributy
val x: String by ValDelegate()

class ValDelegate : ReadOnlyProperty<Nothing?, String> {
    override fun getValue (thisRef: Nothing?, property: KProperty<*>) = "Test"
}

val sq: Double by lazy {
    println("First time")
    kotlin.math.sqrt(2.toDouble())
}

fun main () {
    Delegate(Implementation()).f()
    println(x)
    println("$sq, then $sq")
}