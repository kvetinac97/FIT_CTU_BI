package heap

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PriorityQueueTest {

    @Test
    fun testEmpty () {
        val q = PriorityQueue()
        assertTrue { q.isEmpty() }
    }

    @Test
    fun testSomeElements () {
        val q = PriorityQueue(5, 4.0, 1.0, 5.0)
        assertFalse { q.isEmpty() }
        assertTrue { q.contains(4.0) }
        assertFalse { q.contains(3.2) }
        assertTrue { q.add(3.2) }
        assertFalse { q.addAll(arrayOf(6.0, 3.0)) }
        assertTrue { q.contains(3.2) }
        println(q.toString())
        assertTrue { q.contains(6.0) }
        assertFalse { q.contains(3.0) }
        assertEquals( q.poll(), 6.0 )
        assertEquals( q.remove(), 5.0 )
        assertTrue { q.remove(3.2) }
        assertFalse { q.remove(4.1) }
        assertTrue { q.offer(4.1) }
        assertEquals( q.remove(), 4.1 )
        q.clear()
        assertTrue { q.isEmpty() }
    }

}