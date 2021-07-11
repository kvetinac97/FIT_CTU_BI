package wrzecond

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Server {
    companion object {
        @JvmStatic
        fun main (args: Array<String>) {
            ObjectMapper().registerModule(KotlinModule())
            SpringApplication.run(Server::class.java, *args)
        }
    }
}