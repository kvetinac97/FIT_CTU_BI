/*
    Přidáním (override) val, var nastavím třídě její property
 */
open class Demo (var primaryConstructorParam: String) {

    init {
        primaryConstructorParam = primaryConstructorParam.toUpperCase()
    }

    constructor () : this("empty") {
        primaryConstructorParam += "test"
    }

    open fun testget () = 5
    open val x: Double
        get() = testget().toDouble()

}

class Override : Demo() {
    init {
        primaryConstructorParam += "over"
    }

    override fun testget () = 8

    override val x = 6.9
}

fun main () {
    val demo1 = Demo()
    val demo2 = Demo("interesting")
    val demo3: Demo = Override()

    println(
        demo1.primaryConstructorParam + " " + demo1.testget() + "\n" +
        demo2.primaryConstructorParam + " " + demo2.testget() + "\n" +
        demo3.primaryConstructorParam + " " + demo3.testget()
    )
}