package io.dopamine.integration.mvc.test.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/test/ok")
    fun testOk(): Map<String, String> = mapOf("message" to "pong")

    @GetMapping("/test/error")
    fun testError(): Nothing = throw IllegalStateException("fail!")
}
