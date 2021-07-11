// Dummy DSL to build string
// notice extension function as parameter
// notice apply expands to val sb = StringBuilder() sb.buildAction() return sb
fun buildStr (buildAction: StringBuilder.() -> Unit)
    = StringBuilder().apply(buildAction).toString()

fun StringBuilder.text (str: () -> String)
    = apply { append(str()) }

data class Point2D (var x: Int, var y: Int)

fun main () {
    val str = buildStr {
        text { "Hello " }
        text { "world!" }
    }
    println(str)

    val p = Point2D(12, 15)
    println(p)
    with(p) {
        x=0
        y=0
    }
    println(p)
}