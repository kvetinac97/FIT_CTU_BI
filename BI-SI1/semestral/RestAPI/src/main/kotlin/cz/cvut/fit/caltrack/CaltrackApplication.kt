package cz.cvut.fit.caltrack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/** Main application class */
@SpringBootApplication
class CaltrackApplication

/** Start method */
fun main(args: Array<String>) {
    runApplication<CaltrackApplication>(*args)
}
