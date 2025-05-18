package io.dopamine.demo.response

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.response.code.ErrorCode
import io.dopamine.response.code.SuccessCode
import io.dopamine.response.format.TimestampFormat
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
class ResponseWrappingTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) : FunSpec({

    fun assertWrappedResponse(
        path: String,
        expectedStatus: Int = 200,
        dataAssert: (JsonNode) -> Unit
    ) {
        val result = mockMvc.get(path).andReturn()
        result.response.status shouldBe expectedStatus
        val json = objectMapper.readTree(result.response.contentAsString)

        // 공통 필드 검증
        val code = json["code"] ?: fail("Missing 'code'")
        val message = json["message"] ?: fail("Missing 'message'")
        val timestamp = json["timestamp"] ?: fail("Missing 'timestamp'")
        val meta = json["meta"] ?: fail("Missing 'meta'")
        val traceId = meta["traceId"] ?: fail("Missing 'meta.traceId'")

        if (expectedStatus == 200) {
            code.asText() shouldBe SuccessCode.SUCCESS.code
            message.asText() shouldBe SuccessCode.SUCCESS.message
        }

        traceId.asText().shouldNotBeBlank()
        try {
            LocalDateTime.parse(timestamp.asText(), TimestampFormat.ISO_8601.formatter())
        } catch (e: Exception) {
            fail("Invalid timestamp format: ${timestamp.asText()}")
        }

        dataAssert(json["data"])
    }

    test("String 응답도 자동으로 감싸지고 traceId 포함") {
        assertWrappedResponse("/sample/string") { data ->
            data.asText() shouldBe "hello-world"
        }
    }

    test("직접 반환한 DopamineResponse는 중복 래핑 없이 meta 포함") {
        assertWrappedResponse("/sample/wrapped") { data ->
            data["id"].asText() shouldBe "user-id"
            data["name"].asText() shouldBe "user-name"
        }
    }

    test("일반 DTO도 자동 래핑되어 meta 포함") {
        assertWrappedResponse("/sample/dto") { data ->
            data["id"].asText() shouldBe "user-id"
            data["name"].asText() shouldBe "user-name"
        }
    }

    test("null 반환도 DopamineResponse로 감싸짐") {
        assertWrappedResponse("/sample/null") { data ->
            data.isNull shouldBe true
        }
    }

    test("예외 발생 시도 DopamineResponse 형식으로 에러 응답") {
        val result = mockMvc.get("/sample/error").andReturn()
        val json = objectMapper.readTree(result.response.contentAsString)

        result.response.status shouldBe 400
        json["code"].asText() shouldBe ErrorCode.VALIDATION_FAILED.code
        json["message"].asText() shouldBe "test exception"
        json["meta"]["traceId"].asText().shouldNotBeBlank()

        try {
            LocalDateTime.parse(json["timestamp"].asText(), TimestampFormat.ISO_8601.formatter())
        } catch (e: Exception) {
            fail("timestamp '${json["timestamp"].asText()}'는 ISO_8601 포맷이 아닙니다")
        }
    }

    test("ResponseEntity로 감싼 응답은 자동 래핑되지 않는다") {
        val result = mockMvc.get("/sample/entity").andReturn()

        result.response.status shouldBe 200
        result.response.contentAsString shouldBe "entity-response"
    }
})
