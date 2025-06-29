package io.dopamine.response.common.metadata

import io.dopamine.core.code.CommonErrorCode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus

class CommonErrorMetadataProviderTest :
    FunSpec({

        val provider = CommonErrorMetadataProvider()

        test("should support all CommonErrorCode entries") {
            CommonErrorCode.entries.forEach {
                provider.supports(it) shouldBe true
            }
        }

        test("should return metadata for INVALID_REQUEST") {
            val metadata = provider.provide(CommonErrorCode.INVALID_REQUEST)

            metadata.code shouldBe "INVALID_REQUEST"
            metadata.httpStatus shouldBe HttpStatus.BAD_REQUEST
            metadata.messageKey shouldBe "dopamine.error.valid.400"
            metadata.defaultMessage shouldBe "The request is invalid."
        }
    })
