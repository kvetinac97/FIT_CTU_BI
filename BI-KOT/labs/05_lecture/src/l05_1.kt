
data class Phone   (val model: String, val price: Int)
data class NPerson (val name: String, val phone: Phone? = null) {
    val phoneModel: String?
        get() = phone?.model // nullable operátor
}

fun safeEquals (left: NPerson, right: Any?) : Boolean {
    val rightSafe = right as? NPerson ?: return false
    return left == rightSafe
}

fun main () {
    val alice = NPerson("Alice")
    val bob   = NPerson("Bob", Phone("iPhone 4s", 25000))
    println(alice.phoneModel)
    println(alice.phoneModel ?: "No phone") // elvis
    println(bob.phoneModel)

    println(safeEquals(alice, "Not"))
    println(safeEquals(alice, alice))

    alice.phone?.let { println(it) } // if (phone != null) println(phone)

    alice.phone ?: throw IllegalArgumentException("No phone")
    println(alice.phone)
}