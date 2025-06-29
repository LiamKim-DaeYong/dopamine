package io.dopamine.response.common.metadata

import io.dopamine.core.code.CommonSuccessCode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus

class CommonSuccessMetadataProviderTest :
    FunSpec({

        val provider = CommonSuccessMetadataProvider()

        test("should support all CommonSuccessCode entries") {
            CommonSuccessCode.entries.forEach {
                provider.supports(it) shouldBe true
            }
        }

        test("should return metadata for CREATED") {
            val metadata = provider.provide(CommonSuccessCode.CREATED)

            metadata.code shouldBe "CREATED"
            metadata.httpStatus shouldBe HttpStatus.CREATED
            metadata.messageKey shouldBe "dopamine.success.201"
            metadata.defaultMessage shouldBe "Resource has been created."
        }
    })
