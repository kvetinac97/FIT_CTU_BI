package elements

// Matice znaků / pole stringů
interface Elem {
    // Abstraktní getter a setter
    val content: Array<String>

    // Výška a šířka jsou jasně určeny
    val width: Int
        get() = content[0].length
    val height: Int
        get() = content.size
}

// Hezčí výpis
abstract class AElem : Elem {
    override fun toString() = content.joinToString(separator = "\n")
}

// Přetížením proměnné se automaticky přepíší gettery a settery
open class BasicElem (override val content: Array<String>) : AElem()
open class CharElem (c : Char, w: Int, h: Int) : BasicElem(Array(size = h) { c.toString().repeat(w) })
class EmptyElem(w: Int, h: Int) : CharElem(' ', w, h)

// === Rozšíření funkcí pro výpis

// pole má přetížený operátor + pro napojení polí za sebe
infix fun Elem.above(that: Elem) = BasicElem(this.content + that.content)
infix fun Elem.below(that: Elem) = that.above(this)

// předpokládáme stejné výšky
infix fun Elem.beside(that: Elem) = BasicElem (
    Array(size = this.height) { ind -> this.content[ind] + that.content[ind] }
)

// ===

// Rozšiřování textového spanu
fun Elem.widen       (newWidth: Int)  = this beside EmptyElem(newWidth - width, height)
fun Elem.topwiden    (newWidth: Int)  = EmptyElem(newWidth - width, height) beside this
fun Elem.heighten    (newHeight: Int) = this above EmptyElem(width, newHeight - height)
fun Elem.topheighten (newHeight: Int) = EmptyElem(width, newHeight - height) above this

// Pomocná třída pro aktuální směr spirály (RightLeft, TopBottom, LeftRight, BottomTop)
enum class Direction (private val i: Int) {
    RL(0), TB(1), LR(2), BT(3);
    operator fun plus (j: Int) : Direction = values()[(i+j) % values().size]
}

// Výpis spirály samotné
fun spiral (c: Char, n: Int, direction: Direction = Direction.RL) : Elem {
    if (n == 1)
        return CharElem(c, 1, 1)

    val subspiral = spiral(c, n - 1, direction + 1)
    return when (direction) {
        Direction.RL -> subspiral.widen(n) below CharElem(c, n, 1)
        Direction.TB -> CharElem(c, 1, n) beside subspiral.topheighten(n)
        Direction.LR -> CharElem(c, n, 1) below subspiral.topwiden(n)
        Direction.BT -> subspiral.heighten(n) beside CharElem(c, 1, n)
    }
}

/* Výpis:

n = 1

*

n = 2

**
*

n = 3

***
*
**

n = 4

****
*
* *
***

n = 5

*****
*
* **
*  *
****

n = 6

******
*
* ***
* * *
*   *
*****

n = 7

*******
*
* ****
* *  *
* ** *
*    *
******

n = 8

********
*
* *****
* *   *
* * * *
* *** *
*     *
*******

n = 9

*********
*
* ******
* *    *
* * ** *
* *  * *
* **** *
*      *
********

n = 10

**********
*
* *******
* *     *
* * *** *
* * * * *
* *   * *
* ***** *
*       *
*********

 */

// Program samotný
fun main () {
    for (i in 1 .. 10) {
        println("n = $i\n")
        println(spiral('*', i))
        println()
    }
}
