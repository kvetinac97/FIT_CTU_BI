// Krátké funkce (jednořádkové)
fun max (a: Int, b: Int) = if (a > b) a else b

// Implicitne je vse public
data class Human(val name: String, val age: Int? = null) {
    val isOld: Boolean
    get() {
        return age != null && age > 70
    }
}

// Plná funkce
fun main () {
    // Nepouzivat val pokud to neni treba
    val a = 3
    val b = 5

    val greater = if (a > b) a else b
    println("$greater ${max(a, b)}")

    // Immutable objekty muzou byt mutable
    val test = arrayListOf("Test")
    // Do stringu lze vlozit take volani funkce ( v {} )
    test.add("Second ${if (a > b) a else b}")

    val john = Human("John", 128)
    println("John? $john ${john.isOld}")
}