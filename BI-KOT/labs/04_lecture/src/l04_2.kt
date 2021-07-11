// Pomocny interface
interface MouseListener {
    fun onClicked  (c: Int)
    fun onScrolled (e: String, s: Int)
}

class MyClass {
    interface Wow {
        fun salut () : String
    }
    object First : Wow {
        override fun salut () = "Pozdrav 1"
    }
    // Ekvivalent "static"
    companion object Second : Wow {
        override fun salut () = "Pozdrav 2"
    }
}

fun main () {
    // K teto promenne se dostaneme i z anonymni funkce
    var clicks = 6

    // Klicove slovo objekt
    val listener = object : MouseListener {
        override fun onClicked (s: Int) { clicks += s }
        override fun onScrolled(e: String, s: Int) = println("Scroll $e - $s")
    }

    listener.onClicked(12)
    println(clicks)
    listener.onScrolled("Nove", 128)

    // Nemusime mit rodice
    val point = object {
        val x = 5
        val y = 10
    }
    println("[${point.x},${point.y}]")

    // Companion object
    println(MyClass.First.salut())

    println(MyClass.Second.salut()) // je totez jako
    println(MyClass.salut())
}

