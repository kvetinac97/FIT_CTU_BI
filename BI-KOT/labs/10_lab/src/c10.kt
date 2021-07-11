import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

// Kanál generující přirozená čísla
@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.naturalNumbers () : ReceiveChannel<Int> {
    var c = 2
    return produce {
        while (isActive)
            send(c++)
    }
}

// Funkce vyfiltruje z kanálu násobky čísla n
@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.filter (n: Int, c: ReceiveChannel<Int>) : ReceiveChannel<Int> {
    return produce {
        while (isActive) {
            val x = c.receive()
            if (x % n != 0)
                send(x)
        }
    }
}

// Funkce produkující kanál prvočísel
@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.prime () : ReceiveChannel<Int> {
    var filter = naturalNumbers()
    return produce {
        while (isActive) {
            val e = filter.receive()
            filter = filter(e, filter)
            send(e)
        }
    }
}

// Pomocná funkce pro nalezení maximálního prvočísla v dané době
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun maxPrime (timeout: Long) : Int {
    var max = 0
    withTimeoutOrNull(timeout) {
        prime().consumeEach {
            if ( it < 1000 )
                println(it)
            max = it
        }
    }
    return max
}

fun main () {
    runBlocking {
        println("Max: ${maxPrime(3000)}")
    }
}