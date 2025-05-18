package io.dopamine.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DopamineDemoApplication

fun main(args: Array<String>) {
    runApplication<DopamineDemoApplication>(*args)
}
