package io.dopamine.response.exception

import io.dopamine.response.code.ErrorCode
import io.dopamine.response.code.ResponseCode
import io.dopamine.response.model.DopamineResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.time.LocalDateTime

@ConditionalOnProperty(
    name = ["dopamine.response.formatting.enabled"],
    havingValue = "true",
    matchIfMissing = true,
)
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(DopamineException::class)
    fun handleDopamineException(e: DopamineException): ResponseEntity<DopamineResponse<Unit>> =
        buildErrorResponse(e.responseCode)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<DopamineResponse<Unit>> {
        val fieldErrors =
            e.bindingResult.fieldErrors.map {
                mapOf("field" to it.field, "message" to (it.defaultMessage ?: "잘못된 값입니다."))
            }
        return buildErrorResponse(ErrorCode.VALIDATION_FAILED, mapOf("errors" to fieldErrors))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<DopamineResponse<Unit>> =
        buildErrorResponse(ErrorCode.VALIDATION_FAILED, e.message ?: "요청이 유효하지 않습니다.")

    @ExceptionHandler(RuntimeException::class)
    fun handleUnexpectedRuntime(e: RuntimeException): ResponseEntity<DopamineResponse<Unit>> =
        buildErrorResponse(ErrorCode.INTERNAL_ERROR, mapOf("exception" to (e::class.simpleName ?: "RuntimeException")))

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFound(e: NoHandlerFoundException): ResponseEntity<DopamineResponse<Unit>> =
        buildErrorResponse(ErrorCode.RESOURCE_NOT_FOUND)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowed(e: HttpRequestMethodNotSupportedException): ResponseEntity<DopamineResponse<Unit>> =
        buildErrorResponse(ErrorCode.METHOD_NOT_ALLOWED)

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(e: AccessDeniedException): ResponseEntity<DopamineResponse<Unit>> =
        buildErrorResponse(ErrorCode.ACCESS_DENIED)

    private fun buildErrorResponse(
        code: ResponseCode,
        message: String,
        meta: Map<String, Any>? = null,
    ): ResponseEntity<DopamineResponse<Unit>> =
        ResponseEntity.status(code.httpStatus).body(
            DopamineResponse(
                code = code.code,
                message = message,
                data = null,
                timestamp = LocalDateTime.now().toString(),
                meta = meta,
            ),
        )

    private fun buildErrorResponse(
        code: ResponseCode,
        meta: Map<String, Any>? = null,
    ): ResponseEntity<DopamineResponse<Unit>> = buildErrorResponse(code, code.message, meta)
}
