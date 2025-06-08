package io.dopamine.response.common.factory

import io.dopamine.response.common.code.DefaultSuccessCode
import io.dopamine.response.common.config.CustomResponseCode
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.format.TimestampFormat
import io.dopamine.test.support.assertion.ExpectedResponse
import io.dopamine.test.support.assertion.shouldBeSuccessWith
import io.dopamine.test.support.factory.DopamineResponseFactoryFixtures
import io.dopamine.test.support.factory.DopamineResponseFactoryFixtures.messageResolverWith
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus
import java.time.format.DateTimeFormatter

class DopamineResponseFactoryTest :
    FunSpec({

        val defaultTraceId = "test-trace-id"
        val formatter: DateTimeFormatter = TimestampFormat.ISO_8601.formatter()

        val baseMessageResolver =
            messageResolverWith(
                "dopamine.success.200" to "Request was successful.",
                "dopamine.success.201" to "Resource has been created.",
                "dopamine.error.500" to "Internal server error.",
            )

        val defaultProps =
            ResponseProperties(
                includeMeta = true,
                timestampFormat = TimestampFormat.ISO_8601,
            )

        lateinit var factory: DopamineResponseFactory

        beforeTest {
            factory =
                DopamineResponseFactoryFixtures.dummy(
                    props = defaultProps,
                    messageResolver = baseMessageResolver,
                )
        }

        context("success(...)") {

            test("should include traceId and timestamp when called with default settings") {
                val response = factory.success("hello", meta = mapOf("traceId" to defaultTraceId))

                response shouldBeSuccessWith
                    ExpectedResponse(
                        code = DefaultSuccessCode.SUCCESS.code,
                        message = DefaultSuccessCode.SUCCESS.defaultMessage,
                        data = "hello",
                        traceId = defaultTraceId,
                        formatter = formatter,
                    )
            }

            test("should apply custom response code and message when defined in properties") {
                val customProps =
                    defaultProps.copy(
                        codes = listOf(CustomResponseCode(200, "Custom200", "Custom Success")),
                    )

                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = customProps,
                        messageResolver = baseMessageResolver,
                    )

                val response = factory.success("hello", meta = mapOf("traceId" to defaultTraceId))

                response shouldBeSuccessWith
                    ExpectedResponse(
                        code = "Custom200",
                        message = "Custom Success",
                        data = "hello",
                        traceId = defaultTraceId,
                        formatter = formatter,
                    )
            }

            test("should return null meta when includeMeta is false") {
                val noMetaProps = defaultProps.copy(includeMeta = false)

                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = noMetaProps,
                        messageResolver = baseMessageResolver,
                    )

                val response = factory.success("data", meta = null)
                response.meta shouldBe null
            }
        }

        context("of(...)") {

            test("should fallback to SuccessCode when no custom code or message is provided") {
                val response =
                    factory.of(
                        data = "default case",
                        status = HttpStatus.CREATED,
                        meta = mapOf("traceId" to defaultTraceId),
                    )

                response shouldBeSuccessWith
                    ExpectedResponse(
                        code = DefaultSuccessCode.CREATED.code,
                        message = DefaultSuccessCode.CREATED.defaultMessage,
                        data = "default case",
                        traceId = defaultTraceId,
                        formatter = formatter,
                    )
            }
        }
    })
