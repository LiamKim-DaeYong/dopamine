package io.dopamine.response.common.metadata

import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.code.CustomResponseCode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus

class DefaultResponseCodeRegistryTest :
    FunSpec({

        val predefined =
            listOf(
                ResponseCodeMetadata(
                    code = "SUCCESS",
                    httpStatus = HttpStatus.OK,
                    messageKey = "success.200",
                    defaultMessage = "Success",
                ),
            )

        val custom =
            listOf(
                CustomResponseCode(
                    code = "CUSTOM_SUCCESS",
                    httpStatus = 200,
                    defaultMessage = "Custom OK",
                ),
            )

        val registry = DefaultResponseCodeRegistry(predefined, custom)

        test("should return predefined metadata when code exists") {
            val result = registry.get("SUCCESS")
            result?.code shouldBe "SUCCESS"
            result?.httpStatus shouldBe HttpStatus.OK
        }

        test("should return custom metadata from CustomResponseCode") {
            val result = registry.get("CUSTOM_SUCCESS")
            result?.code shouldBe "CUSTOM_SUCCESS"
            result?.httpStatus shouldBe HttpStatus.OK
            result?.defaultMessage shouldBe "Custom OK"
        }

        test("should return null when code is not found") {
            registry.get("NOT_EXIST") shouldBe null
        }

        test("should return all metadata values") {
            registry.getAll().size shouldBe 2
        }

        test("should support get by ResponseCode interface") {
            val code =
                object : ResponseCode {
                    override val code: String = "SUCCESS"
                }
            registry.get(code)?.code shouldBe "SUCCESS"
        }
    })
