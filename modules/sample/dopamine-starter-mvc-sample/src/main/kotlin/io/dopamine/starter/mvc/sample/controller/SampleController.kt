package io.dopamine.starter.mvc.sample.controller

import io.dopamine.response.common.model.DopamineResponse
import io.dopamine.starter.mvc.sample.dto.SampleDetailDto
import io.dopamine.starter.mvc.sample.dto.SampleResponseDto
import io.dopamine.starter.mvc.sample.dto.SampleStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
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

    @GetMapping("/page")
    fun getPage(): Page<SampleResponseDto> {
        val pageRequest = PageRequest.of(0, 3)
        val content =
            listOf(
                getDto(),
                getDto().copy(id = 43L),
                getDto().copy(id = 44L),
            )
        return PageImpl(content, pageRequest, 10)
    }

    @GetMapping("/entity")
    fun getEntity(): ResponseEntity<SampleResponseDto> = ResponseEntity.status(HttpStatus.CREATED).body(getDto())

    @GetMapping("/dopamine")
    fun getDopamine(): DopamineResponse<SampleResponseDto> =
        DopamineResponse(
            code = "SUCCESS",
            message = "Manual dopamine response",
            data = getDto(),
            timestamp = LocalDateTime.now().toString(),
            meta = emptyMap(),
        )

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun noContent(): String? = null
}
