package io.dopamine.response.core.factory

import io.dopamine.core.code.SuccessCode
import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.config.ResponseProperties.CustomResponseCode
import io.dopamine.response.core.format.TimestampFormat
import io.dopamine.response.core.model.DopamineResponse
import io.dopamine.response.core.trace.TraceContext
import io.dopamine.test.support.assertion.ExpectedResponse
import io.dopamine.test.support.assertion.shouldBeSuccessWith
import io.dopamine.test.support.factory.DopamineResponseFactoryFixtures
import io.dopamine.test.support.trace.TraceContextFixtures
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus
import java.time.format.DateTimeFormatter

class DopamineResponseFactoryTest : FunSpec({

    val defaultTraceId = "test-trace-id"
    val formatter: DateTimeFormatter = TimestampFormat.ISO_8601.formatter()

    lateinit var factory: DopamineResponseFactory
    lateinit var context: TraceContext

    beforeTest {
        factory = DopamineResponseFactoryFixtures.dummy(
            props = ResponseProperties(
                includeMeta = true,
                timestampFormat = TimestampFormat.ISO_8601
            ),
            traceId = defaultTraceId
        )
        context = TraceContextFixtures.dummy()
    }

    context("success(...)") {

        test("should include traceId and timestamp when called with default settings") {
            val response: DopamineResponse<String> = factory.success("hello", context)

            response shouldBeSuccessWith ExpectedResponse(
                code = SuccessCode.SUCCESS.code,
                message = SuccessCode.SUCCESS.defaultMessage,
                data = "hello",
                traceId = defaultTraceId,
                formatter = formatter
            )
        }

        test("should apply custom response code and message when defined in properties") {
            val props = ResponseProperties(
                includeMeta = true,
                timestampFormat = TimestampFormat.ISO_8601,
                codes = listOf(
                    CustomResponseCode(200, "Custom200", "Custom Success")
                )
            )
            val factory = DopamineResponseFactoryFixtures.dummy(
                props = props,
                traceId = defaultTraceId
            )

            val response = factory.success("hello", context)

            response shouldBeSuccessWith ExpectedResponse(
                code = "Custom200",
                message = "Custom Success",
                data = "hello",
                traceId = defaultTraceId,
                formatter = formatter
            )
        }

        test("should return null meta when includeMeta is false") {
            val factory = DopamineResponseFactoryFixtures.dummy(
                props = ResponseProperties(includeMeta = false),
                traceId = defaultTraceId
            )

            val response = factory.success("data", context)

            response.meta shouldBe null
        }
    }

    context("of(...)") {

        test("should fallback to SuccessCode when no custom code or message is provided") {
            val response = factory.of(
                data = "default case",
                status = HttpStatus.CREATED,
                context = context
            )

            response shouldBeSuccessWith ExpectedResponse(
                code = SuccessCode.CREATED.code,
                message = SuccessCode.CREATED.defaultMessage,
                data = "default case",
                traceId = defaultTraceId,
                formatter = formatter
            )
        }
    }
})
