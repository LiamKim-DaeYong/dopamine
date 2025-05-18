package io.dopamine.demo.response.controller

import io.dopamine.demo.response.dto.SampleUserDto
import io.dopamine.response.exception.ExceptionFactory
import io.dopamine.response.model.DopamineResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample")
class SampleController {
    @GetMapping("/string")
    fun getString(): String = "hello-world"

    @GetMapping("/dto")
    fun getUserRaw(): SampleUserDto =
        SampleUserDto(
            id = "user-id",
            name = "user-name",
        )

    @GetMapping("/wrapped")
    fun getUserWrapped(): DopamineResponse<SampleUserDto> {
        return DopamineResponse.Companion.success(
            SampleUserDto(
                id = "user-id",
                name = "user-name",
            ),
        )
    }

    @GetMapping("/error")
    fun throwException(): String {
        throw ExceptionFactory.Validation.badRequest("test exception")
    }

    @GetMapping("/entity")
    fun getEntity(): ResponseEntity<String> = ResponseEntity.ok("entity-response")

    @GetMapping("/null")
    fun returnNull(): String? = null
}
