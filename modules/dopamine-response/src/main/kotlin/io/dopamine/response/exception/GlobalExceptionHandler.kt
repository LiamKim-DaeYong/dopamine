package io.dopamine.response.exception

import io.dopamine.response.code.ErrorCode
import io.dopamine.response.code.ResponseCode
import io.dopamine.response.code.ResponseCodeRegistry
import io.dopamine.response.model.DopamineResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler(
    private val responseCodeRegistry: ResponseCodeRegistry,
    private val response: HttpServletResponse,
) {
    @ExceptionHandler(DopamineException::class)
    fun handleDopamineException(e: DopamineException): DopamineResponse<Unit> =
        buildErrorResponse(e.responseCode, e.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): DopamineResponse<Unit> {
        val fieldErrors =
            e.bindingResult.fieldErrors.map {
                mapOf("field" to it.field, "message" to (it.defaultMessage ?: "잘못된 값입니다."))
            }
        return buildErrorResponse(ErrorCode.VALIDATION_FAILED, mapOf("errors" to fieldErrors))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): DopamineResponse<Unit> =
        buildErrorResponse(ErrorCode.VALIDATION_FAILED, e.message ?: "요청이 유효하지 않습니다.")

    @ExceptionHandler(RuntimeException::class)
    fun handleUnexpectedRuntime(e: RuntimeException): DopamineResponse<Unit> =
        buildErrorResponse(
            ErrorCode.INTERNAL_ERROR,
            e.message,
            mapOf(
                "exception" to (e::class.simpleName ?: "RuntimeException"),
            ),
        )

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFound(e: NoHandlerFoundException): DopamineResponse<Unit> =
        buildErrorResponse(ErrorCode.RESOURCE_NOT_FOUND)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowed(e: HttpRequestMethodNotSupportedException): DopamineResponse<Unit> =
        buildErrorResponse(ErrorCode.METHOD_NOT_ALLOWED)

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(e: AccessDeniedException): DopamineResponse<Unit> =
        buildErrorResponse(ErrorCode.ACCESS_DENIED)

    private fun buildErrorResponse(
        code: ResponseCode,
        message: String? = null,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<Unit> {
        val resolved = responseCodeRegistry.resolve(code)
        response.status = resolved.httpStatus.value()
        return DopamineResponse.error(resolved, message ?: resolved.message, meta)
    }

    private fun buildErrorResponse(
        code: ResponseCode,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<Unit> = buildErrorResponse(code, null, meta)
}
