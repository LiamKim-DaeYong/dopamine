package io.dopamine.response.common.metadata

import io.dopamine.core.code.ResponseCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus

class DefaultResponseMetadataResolverTest :
    FunSpec({

        val provider =
            object : ResponseMetadataProvider {
                override fun supports(code: ResponseCode): Boolean = code.code == "MATCH"

                override fun provide(code: ResponseCode): ResponseCodeMetadata =
                    ResponseCodeMetadata(
                        code = code.code,
                        httpStatus = HttpStatus.ACCEPTED,
                        messageKey = "custom.key",
                        defaultMessage = "Accepted",
                    )
            }

        val resolver = DefaultResponseMetadataResolver(listOf(provider))

        test("should resolve metadata when provider supports code") {
            val code =
                object : ResponseCode {
                    override val code: String = "MATCH"
                }

            val metadata = resolver.resolve(code)

            metadata.code shouldBe "MATCH"
            metadata.httpStatus shouldBe HttpStatus.ACCEPTED
            metadata.messageKey shouldBe "custom.key"
            metadata.defaultMessage shouldBe "Accepted"
        }

        test("should throw when no provider supports code") {
            val code =
                object : ResponseCode {
                    override val code: String = "UNKNOWN"
                }

            shouldThrow<IllegalStateException> {
                resolver.resolve(code)
            }
        }
    })
