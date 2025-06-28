package io.dopamine.response.common.factory

import io.dopamine.core.code.CommonErrorCode
import io.dopamine.core.code.CommonSuccessCode
import io.dopamine.core.code.ResponseCode
import io.dopamine.core.format.TimestampFormat
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.metadata.CommonErrorMetadata
import io.dopamine.response.common.metadata.CommonSuccessMetadata
import io.dopamine.response.common.metadata.ResponseCodeMetadata
import io.dopamine.response.common.support.DopamineResponseFactoryFixtures
import io.dopamine.response.common.support.DummyCodeRegistry
import io.dopamine.response.common.support.DummyMessageResolver
import io.dopamine.response.common.support.ExpectedResponse
import io.dopamine.response.common.support.StaticMetaContributor
import io.dopamine.response.common.support.shouldBeSuccessWith
import io.dopamine.test.support.util.FixedClock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DopamineResponseFactoryTest :
    FunSpec({

        val defaultTraceId = "test-trace-id"
        val formatter: DateTimeFormatter = TimestampFormat.ISO_8601.formatter()

        val defaultProps =
            ResponseProperties(
                includeMeta = true,
                timestampFormat = TimestampFormat.ISO_8601,
            )

        val baseMessageResolver = DummyMessageResolver()

        context("success(...)") {
            val staticContributor = StaticMetaContributor(mapOf("traceId" to defaultTraceId))

            test("should build success response with meta") {
                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = defaultProps,
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                        registry = DummyCodeRegistry(CommonSuccessMetadata.values),
                    )

                val response = factory.success("hello")

                response shouldBeSuccessWith
                    ExpectedResponse(
                        code = CommonSuccessCode.SUCCESS.code,
                        message = "Request was successful.",
                        data = "hello",
                        meta = mapOf("traceId" to defaultTraceId),
                        formatter = formatter,
                    )
            }

            test("should apply custom response code and message") {
                val customMetadata =
                    ResponseCodeMetadata(
                        code = "Custom200",
                        httpStatus = HttpStatus.OK,
                        messageKey = null,
                        defaultMessage = "Custom Success",
                    )

                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                        registry = DummyCodeRegistry(listOf(customMetadata)),
                    )

                val response =
                    factory.success(
                        "hello",
                        responseCode =
                            object : ResponseCode {
                                override val code: String = "Custom200"
                            },
                    )

                response shouldBeSuccessWith
                    ExpectedResponse(
                        code = "Custom200",
                        message = "Custom Success",
                        data = "hello",
                        meta = mapOf("traceId" to defaultTraceId),
                        formatter = formatter,
                    )
            }

            test("should return null meta when includeMeta is false") {
                val noMetaProps = defaultProps.copy(includeMeta = false)

                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = noMetaProps,
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                    )

                val response = factory.success("data")
                response.meta shouldBe null
            }

            test("should not include meta when includeMeta is false") {
                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = defaultProps.copy(includeMeta = false),
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                    )

                val response = factory.success("data")
                response.meta shouldBe null
            }
        }

        context("fail(...)") {
            val staticContributor = StaticMetaContributor(mapOf("traceId" to defaultTraceId))

            test("should build failure response with meta") {
                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = defaultProps,
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                        registry = DummyCodeRegistry(CommonErrorMetadata.values),
                    )

                val response = factory.fail(CommonErrorCode.INVALID_REQUEST)

                response shouldBeSuccessWith
                    ExpectedResponse(
                        code = CommonErrorCode.INVALID_REQUEST.code,
                        message = "The request is invalid.",
                        data = null,
                        meta = mapOf("traceId" to defaultTraceId),
                        formatter = formatter,
                    )
            }

            test("should include requestData in meta") {
                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = defaultProps,
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                        registry = DummyCodeRegistry(CommonErrorMetadata.values),
                    )

                val request = mapOf("email" to "test@test.com")

                val response =
                    factory.fail(
                        responseCode = CommonErrorCode.INVALID_REQUEST,
                        requestData = request,
                    )

                response.meta?.get("traceId") shouldBe defaultTraceId
                response.meta?.get("request") shouldBe request
            }

            test("should not include meta when includeMeta is false") {
                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = defaultProps.copy(includeMeta = false),
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                        registry = DummyCodeRegistry(CommonErrorMetadata.values),
                    )

                val response = factory.fail(CommonErrorCode.INTERNAL_SERVER_ERROR)
                response.meta shouldBe null
            }
        }

        context("of(...)") {
            val staticContributor = StaticMetaContributor(mapOf("traceId" to defaultTraceId))

            test("should resolve response using metadata registry") {
                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = defaultProps,
                        messageResolver = baseMessageResolver,
                        contributors = listOf(staticContributor),
                        registry = DummyCodeRegistry(CommonSuccessMetadata.values),
                    )

                val response =
                    factory.of(
                        data = "default case",
                        responseCode = CommonSuccessCode.CREATED,
                    )

                response shouldBeSuccessWith
                    ExpectedResponse(
                        code = CommonSuccessCode.CREATED.code,
                        message = "Resource has been created.",
                        data = "default case",
                        meta = mapOf("traceId" to defaultTraceId),
                        formatter = formatter,
                    )
            }
        }

        context("timestamp formatting") {
            test("should use fixed clock in response timestamp") {
                val fixedTime = Instant.parse("2025-01-01T12:34:56Z")
                val fixedClock = FixedClock.fixed(fixedTime, ZoneId.of("UTC"))

                val factory =
                    DopamineResponseFactoryFixtures.dummy(
                        props = defaultProps,
                        messageResolver = baseMessageResolver,
                        contributors = emptyList(),
                        clock = fixedClock,
                    )

                val response = factory.success("fixed time")
                response.timestamp shouldBe "2025-01-01T12:34:56"
            }
        }
    })
