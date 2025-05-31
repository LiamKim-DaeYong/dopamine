package io.dopamine.starter.mvc.sample.controller

import io.dopamine.starter.mvc.sample.dto.SampleDetailDto
import io.dopamine.starter.mvc.sample.dto.SampleResponseDto
import io.dopamine.starter.mvc.sample.dto.SampleStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/sample")
class SampleController {
    @GetMapping("/string")
    fun getString(): String = "hello world"

    @GetMapping("/dto")
    fun getDto(): SampleResponseDto =
        SampleResponseDto(
            id = 42L,
            name = "Dopamine",
            status = SampleStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            tags = listOf("infra", "spring-boot", "starter"),
            details =
                SampleDetailDto(
                    description = "This is a detailed description of the sample.",
                    score = 87,
                ),
            optionalField = "optional value",
        )

    @GetMapping("/list")
    fun getList(): List<SampleResponseDto> =
        listOf(
            getDto(),
            getDto().copy(id = 43L, name = "Dopamine-2", status = SampleStatus.INACTIVE),
        )

    @GetMapping("/map")
    fun getMap(): Map<String, Any> =
        mapOf(
            "message" to "map response",
            "value" to 123,
            "success" to true,
        )

    @GetMapping("/null")
    fun getNull(): String? = null

    @GetMapping("/error")
    fun getError(): Nothing = throw IllegalStateException("Intentional error for testing.")
}
