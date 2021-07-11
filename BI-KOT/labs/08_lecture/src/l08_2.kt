import java.util.*
import kotlin.math.abs

// Covariant type
class Producer<out T> {
    val value: T = (Scanner(System.`in`).nextLine()) as T

    fun read () = value
    // fun write(x: T) compilation error
}

// Contravariant type
class Consumer<in T> {
    fun write (x: T) = println(x)
    // fun read () : T compilation error
}

// Box
data class Box<T> (val value: T)

fun absNumberWrong (b: Box<Number>) = abs(b.value.toDouble())
fun absNumber (b: Box<out Number>) = abs(b.value.toDouble())

fun main () {
    // This would not be possible with fun a b: Box
    val box = Box(-5)
    // println(absNumberWrong(box) compile error
    println(absNumber(box))

    println("Type text:")
    val x = Producer<String>().read()
    Consumer<String>().write(x)
}