package cz.cvut.fit.biand.async.model

/**
 * Nonoptimal way how to compute forNumber number
 */
class FibonacciComputer {

    fun forNumber (n: Int) : Int {
        return if (n < 2) n else forNumber(n - 1) + forNumber(n - 2)
    }

}
