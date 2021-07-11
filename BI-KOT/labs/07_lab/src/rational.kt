
data class Rational (val nom: Int, val den: Int = 1) {

    // Lazy initializer for normalized value
    val norm: Rational by lazy {
        val gcd = gcd(nom, den)
        Rational(nom / gcd, den / gcd)
    }

             fun inverse    () = Rational(den, nom)
    operator fun unaryMinus () = Rational(-nom, den)

    operator fun plus  (that: Rational) = Rational(nom * that.den + that.nom * den, den * that.den)
    operator fun minus (that: Rational) = this + (-that)
    operator fun times (that: Rational) = Rational(nom * that.nom, den * that.den)
    operator fun div   (that: Rational) = this * that.inverse()

    operator fun plus  (that: Int)      = this + !that
    operator fun minus (that: Int)      = this + -that
    operator fun times (that: Int)      = this * !that
    operator fun div   (that: Int)      = this / !that

    // Safe, as Rational is final and cannot be extended
    // otherwise, we would have to do other::class == Rational::class
    override fun equals (other: Any?) = this === other ||
        ( other is Rational && norm.nom == other.norm.nom && norm.den == other.norm.den )
    override fun hashCode() = nom * den

    override fun toString() = "$nom/$den"

    companion object {
        // Recursive functions need explicit return type
        // calculate greatest common divisor for normalisation
        fun gcd (l: Int, r: Int) : Int =
            if (r == 0) l
            else gcd(r, l % r)
    }
}

// Allow for n +-*/ rational
operator fun Int.plus  (that: Rational) = that + this
operator fun Int.minus (that: Rational) = that - this
operator fun Int.times (that: Rational) = that * this
operator fun Int.div   (that: Rational) = that / this

// We can give own semantics to any operator (used in DSL)
// but we can not extend existing operators
operator fun Int.not() = Rational(this, 1)

fun main () {
    val r1 = Rational(2, 3)
    val r2 = Rational(4, 6)
    println("$r1, $r2, ${r1 == r2}, ${r1 * r2}, ${r1 * 2}, ${3 * r2}")
    println(!1/r1) // smart conversion to Rational ( thanks to operator not ! )
}
