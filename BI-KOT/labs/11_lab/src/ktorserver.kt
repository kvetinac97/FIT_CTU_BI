import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.apache.log4j.BasicConfigurator

fun main () {
    BasicConfigurator.configure()
    embeddedServer(Netty, port = 8080, module = Application::mainModule)
        .start(wait = true)
}

fun Application.mainModule () {
    install(ContentNegotiation) { gson {} }
    routing {
        route("/customers") {
            get("/") {
                call.respond(CustomerFacade.allCustomers())
            }
            get("/{n}") {
                val cust = CustomerFacade.allCustomers().find { it.id == call.parameters["n"]!!.toInt() }
                cust?.let { call.respond(cust) } ?: call.respond(HttpStatusCode.NotFound, "Not found")
            }
        }
    }
}