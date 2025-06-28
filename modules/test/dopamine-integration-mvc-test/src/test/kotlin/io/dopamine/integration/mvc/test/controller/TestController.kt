package io.dopamine.integration.mvc.test.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping("/ok")
    fun testOk(): Map<String, String> = mapOf("message" to "integration-test")

    @GetMapping("/error")
    fun testError(): Nothing = throw IllegalStateException("fail!")

    @GetMapping("/page")
    fun testPage(): Page<String> {
        val content = listOf("a", "b", "c")
        val pageable = PageRequest.of(0, 3)

        return PageImpl(content, pageable, 10)
    }
}
