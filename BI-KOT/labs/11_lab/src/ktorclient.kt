import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

val rootUrl: Url = URLBuilder(
    protocol = URLProtocol.HTTP,
    host = "127.0.0.1",
    port = 8080
).build()

val client = HttpClient(Java) {
    install(JsonFeature) {
        serializer = GsonSerializer {}
    }
}

suspend fun getCustomer (id: Int) : Customer {
    return client.get(rootUrl) {
        url.path("customers/$id")
    }
}

suspend fun getCustomers () : List<Customer> {
    return client.get(rootUrl) {
        url.path("customers/")
    }
}

fun main () {
    runBlocking {
        println(getCustomers())
        println(getCustomer(45))
        try { println(getCustomer(1)) } catch (xc: ClientRequestException) { println("Error response ${xc.response.status.value}") }
    }
}