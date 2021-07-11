package heap

import java.util.*

class PriorityQueue(private val limit: Int = 128, vararg values: Double) : Queue<Double> {

    // constructor part 1 = create with bad indexes
    private val data: Array<Double> = Array(limit) { index -> values.getOrElse(index) { 0.0 } }
    private var lastIndex = -1

    // constructor part 2 = fix bad heap
    init {
        for ( index in 0 until size )
            bubbleUp (index)
        lastIndex = values.size - 1
    }

    private fun bubbleUp (index: Int) {
        if (index == 0) return

        val parentIndex = parentIndex(index)
        if (data[parentIndex] < data[index] ) {
            data.swap(parentIndex, index)
            bubbleUp(parentIndex)
        }
    }
    private fun bubbleDown (index: Int) {
        if (leftChildIndex(index) >= size || rightChildIndex(index) >= size)
            return

        if (data[leftChildIndex(index)] > data[rightChildIndex(index)]) {
            if (data[leftChildIndex(index)] > data[index]) {
                data.swap(leftChildIndex(index), index)
                bubbleDown(leftChildIndex(index))
                return
            }
        }
        else if (data[rightChildIndex(index)] > data[index]) {
            data.swap(rightChildIndex(index), index)
            bubbleDown(rightChildIndex(index))
            return
        }
    }

    override val size: Int
        get() = lastIndex + 1

    // take vybere pouze prvních "size" prvku
    override fun toString() = data.take(size).joinToString(prefix = "[", postfix = "]")

    override fun add(element: Double): Boolean {
        if (size >= limit)
            return false

        data[++lastIndex] = element
        bubbleUp(lastIndex)
        return true
    }

    override fun containsAll(elements: Collection<Double>): Boolean {
        for (element in elements)
            if (!contains(element))
                return false
        return true
    }

    override fun addAll(elements: Collection<Double>): Boolean {
        var result = true
        for (element in elements)
            result = result && add(element)
        return result
    }

    override fun clear() {
        lastIndex = -1
    }

    override fun remove(): Double {
        val ret = data[0]
        data.swap(0, size - 1)
        --lastIndex
        bubbleDown(0)
        return ret
    }

    override fun remove(element: Double): Boolean {
        for (index in 0 until size) {
            if (data[index] == element) {
                data.swap(index, size - 1)
                --lastIndex
                bubbleDown(index)
                return true
            }
        }
        return false
    }

    override fun retainAll(elements: Collection<Double>): Boolean {
        var changed = false
        elements.forEach { changed = changed || remove(it) }
        return changed
    }

    override fun poll(): Double = remove()

    override fun peek(): Double = data[0]

    override fun contains(element: Double): Boolean = data.take(size).contains(element)

    override fun isEmpty(): Boolean = ( lastIndex == -1 )

    override fun iterator(): MutableIterator<Double> = data.take(size).toMutableList().listIterator()

    override fun removeAll(elements: Collection<Double>): Boolean {
        for (element in elements)
            if (!remove(element))
                return false
        return true
    }

    override fun offer(p0: Double): Boolean = add(p0)

    override fun element(): Double = peek()

    // reprezentuje staticka volani tridy
    companion object {
        fun<T> Array<T>.swap (i1: Int, i2: Int) {
            val tmp = this[i1]
            this[i1] = this[i2]
            this[i2] = tmp
        }

        fun parentIndex (childIndex: Int) = (childIndex - 1) / 2
        fun leftChildIndex (parentIndex: Int) = parentIndex * 2 + 1
        fun rightChildIndex (parentIndex: Int) = parentIndex * 2 + 2
    }

}