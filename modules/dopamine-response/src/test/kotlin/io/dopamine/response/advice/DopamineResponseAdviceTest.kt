package io.dopamine.response.advice

import com.fasterxml.jackson.core.type.TypeReference
import io.dopamine.response.config.ResponseProperties
import io.dopamine.response.format.TimestampFormat
import io.dopamine.response.model.DopamineResponse
import io.dopamine.test.support.DummyMethodParameter
import io.dopamine.test.support.ObjectMapperSupport
import io.dopamine.test.support.ResponseEntityMethodParameter
import io.dopamine.test.support.StringMethodParameter
import io.dopamine.test.support.mockRequest
import io.dopamine.test.support.mockResponse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.slf4j.MDC
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter

class DopamineResponseAdviceTest :
    FunSpec({

        val props =
            ResponseProperties().copy(
                enabled = true,
                includeMeta = false,
                timestampFormat = TimestampFormat.COMPACT,
            )

        val objectMapper = ObjectMapperSupport.defaultMapper()
        val advice = DopamineResponseAdvice(props, objectMapper)

        test("응답 본문을 DopamineResponse로 wrapping 한다") {
            // given
            val body = "hello"
            val returnType = DummyMethodParameter()
            val request = mockRequest()
            val response = mockResponse()

            // when
            val result =
                advice.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    StringHttpMessageConverter::class.java,
                    request,
                    response,
                )

            // then
            (result as DopamineResponse<*>).data shouldBe "hello"
            result.meta shouldBe null
            result.timestamp.length shouldBe 15
        }

        test("이미 래핑된 응답은 중복 감싸지 않고 timestamp/meta만 갱신된다") {
            // given
            val oldTimestamp = "20220101T000000"
            val original = DopamineResponse.success("world", timestamp = oldTimestamp, meta = mapOf("note" to "keep"))

            val returnType = DummyMethodParameter()
            val request = mockRequest()
            val response = mockResponse()

            // when
            val result =
                advice.beforeBodyWrite(
                    original,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    StringHttpMessageConverter::class.java,
                    request,
                    response,
                ) as DopamineResponse<*>

            // then
            result.data shouldBe "world"
            result.timestamp shouldNotBe oldTimestamp
            result.meta shouldBe mapOf("note" to "keep")
        }

        test("ResponseEntity 타입은 감싸지 않고 그대로 반환된다") {
            // given
            val body = "hello"
            val returnType = ResponseEntityMethodParameter()
            val request = mockRequest()
            val response = mockResponse()

            // when
            val result =
                advice.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    StringHttpMessageConverter::class.java,
                    request,
                    response,
                )

            // then
            result shouldBe "hello"
        }

        test("String 타입 응답은 JSON 문자열로 직렬화되어야 한다") {
            // given
            val body = "hello"
            val returnType = StringMethodParameter()
            val request = mockRequest()
            val response = mockResponse()

            // when
            val result =
                advice.beforeBodyWrite(
                    body,
                    returnType,
                    MediaType.APPLICATION_JSON,
                    StringHttpMessageConverter::class.java,
                    request,
                    response,
                )

            // then
            val parsed: DopamineResponse<*> =
                objectMapper.readValue(
                    result as String,
                    object : TypeReference<DopamineResponse<*>>() {},
                )
            parsed.data shouldBe "hello"
        }

        test("MDC에 traceId가 있으면 meta에 포함된다") {
            // given
            val traceKey = "X-Trace-ID"
            val traceValue = "abc-123"

            MDC.put(traceKey, traceValue)

            val propsWithMeta =
                props.copy(
                    includeMeta = true,
                    metaOptions =
                        props.metaOptions.copy(
                            includeTraceId = true,
                            traceIdKey = traceKey,
                            traceIdHeader = traceKey,
                        ),
                )
            val adviceWithMeta = DopamineResponseAdvice(propsWithMeta, objectMapper)

            val returnType = DummyMethodParameter()
            val request = mockRequest()
            val response = mockResponse()

            // when
            val result =
                adviceWithMeta.beforeBodyWrite(
                    "hello",
                    returnType,
                    MediaType.APPLICATION_JSON,
                    StringHttpMessageConverter::class.java,
                    request,
                    response,
                ) as DopamineResponse<*>

            // then
            result.meta?.get(traceKey) shouldBe traceValue

            // cleanup
            MDC.clear()
        }
    })
