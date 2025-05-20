package io.dopamine.response.exception

import io.dopamine.response.code.ErrorCode
import io.dopamine.response.code.ResponseCodeRegistry
import io.dopamine.response.config.ResponseProperties
import io.dopamine.response.model.DopamineResponse
import io.dopamine.test.support.DummyMethodParameter
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.servlet.NoHandlerFoundException

class GlobalExceptionHandlerTest :
    FunSpec({

        val props = ResponseProperties()
        val response = MockHttpServletResponse()
        val registry = ResponseCodeRegistry(props)
        val handler = GlobalExceptionHandler(registry, response)

        test("IllegalArgumentException 발생 시 VALIDATION_FAILED 코드로 반환된다") {
            // when
            val ex = IllegalArgumentException("잘못된 입력")
            val result: DopamineResponse<Unit> = handler.handleIllegalArgument(ex)

            // then
            result.code shouldBe ErrorCode.VALIDATION_FAILED.code
            result.message shouldBe "잘못된 입력"
            result.data shouldBe null
            response.status shouldBe ErrorCode.VALIDATION_FAILED.httpStatus.value()
        }

        test("RuntimeException 발생 시 INTERNAL_ERROR 코드와 예외명이 포함된다") {
            // when
            val ex = RuntimeException("뭔가 이상함")
            val result: DopamineResponse<Unit> = handler.handleUnexpectedRuntime(ex)

            // then
            result.code shouldBe ErrorCode.INTERNAL_ERROR.code
            result.message shouldBe "뭔가 이상함"
            result.data shouldBe null
            result.meta!!["exception"] shouldBe "RuntimeException"
            response.status shouldBe ErrorCode.INTERNAL_ERROR.httpStatus.value()
        }

        test("DopamineException 발생 시 커스텀 코드와 메시지로 반환된다") {
            // given
            val ex =
                DopamineException(
                    responseCode = ErrorCode.ACCESS_DENIED,
                    message = "허용되지 않음",
                )

            // when
            val result: DopamineResponse<Unit> = handler.handleDopamineException(ex)

            // then
            result.code shouldBe ErrorCode.ACCESS_DENIED.code
            result.message shouldBe "허용되지 않음"
            result.data shouldBe null
            response.status shouldBe ErrorCode.ACCESS_DENIED.httpStatus.value()
        }

        test("NoHandlerFoundException 발생 시 RESOURCE_NOT_FOUND 코드로 반환된다") {
            // given
            val ex = NoHandlerFoundException("GET", "/missing", HttpHeaders())

            // when
            val result: DopamineResponse<Unit> = handler.handleNotFound(ex)

            // then
            result.code shouldBe ErrorCode.RESOURCE_NOT_FOUND.code
            result.message shouldBe ErrorCode.RESOURCE_NOT_FOUND.message
            result.data shouldBe null
            response.status shouldBe ErrorCode.RESOURCE_NOT_FOUND.httpStatus.value()
        }

        test("HttpRequestMethodNotSupportedException 발생 시 METHOD_NOT_ALLOWED 코드로 반환된다") {
            // given
            val ex = HttpRequestMethodNotSupportedException("POST", listOf("GET"))

            // when
            val result: DopamineResponse<Unit> = handler.handleMethodNotAllowed(ex)

            // then
            result.code shouldBe ErrorCode.METHOD_NOT_ALLOWED.code
            result.message shouldBe ErrorCode.METHOD_NOT_ALLOWED.message
            result.data shouldBe null
            response.status shouldBe ErrorCode.METHOD_NOT_ALLOWED.httpStatus.value()
        }

        test("MethodArgumentNotValidException 발생 시 VALIDATION_FAILED 코드와 필드 에러를 반환한다") {
            // given
            val target = Any()
            val bindingResult = BeanPropertyBindingResult(target, "request")
            bindingResult.addError(FieldError("request", "username", "이름은 필수입니다."))

            val parameter = DummyMethodParameter()
            val ex = MethodArgumentNotValidException(parameter, bindingResult)

            // when
            val result: DopamineResponse<Unit> = handler.handleValidationException(ex)

            // then
            result.code shouldBe ErrorCode.VALIDATION_FAILED.code
            result.message shouldBe ErrorCode.VALIDATION_FAILED.message
            response.status shouldBe ErrorCode.VALIDATION_FAILED.httpStatus.value()

            val errors = result.meta?.get("errors") as? List<*> ?: error("meta.errors is missing")
            val first = errors.first() as Map<*, *>
            first["field"] shouldBe "username"
            first["message"] shouldBe "이름은 필수입니다."
        }
    })
