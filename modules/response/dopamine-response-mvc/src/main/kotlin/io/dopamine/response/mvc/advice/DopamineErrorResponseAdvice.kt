package io.dopamine.response.mvc.advice

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.dopamine.core.code.CommonErrorCode
import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.exception.DopamineException
import io.dopamine.response.common.factory.DopamineResponseFactory
import io.dopamine.response.common.metadata.ResponseMetadataResolver
import io.dopamine.response.common.model.DopamineResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class DopamineErrorResponseAdvice(
    private val factory: DopamineResponseFactory,
    private val resolver: ResponseMetadataResolver,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BindException::class)
    fun handleBindingException(e: BindException): ResponseEntity<DopamineResponse<Any?>> {
        val errorCode = CommonErrorCode.INVALID_REQUEST
        val requestObject = e.bindingResult.target

        val status = resolveHttpStatus(errorCode)

        val response = factory.fail(responseCode = errorCode, requestData = requestObject)
        return ResponseEntity.status(status).body(response)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(
        e: MissingServletRequestParameterException,
    ): ResponseEntity<DopamineResponse<Any?>> {
        val errorCode = CommonErrorCode.BAD_REQUEST

        val status = resolveHttpStatus(errorCode)

        val response =
            factory.fail(
                responseCode = errorCode,
                requestData = mapOf("missingParameter" to e.parameterName),
            )
        return ResponseEntity.status(status).body(response)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        e: HttpMessageNotReadableException,
    ): ResponseEntity<DopamineResponse<Any?>> {
        val errorCode = CommonErrorCode.INVALID_REQUEST
        val causeMessage =
            when (val cause = e.cause) {
                is InvalidFormatException -> cause.originalMessage
                else -> cause?.message ?: "Unreadable request body"
            }

        val status = resolveHttpStatus(errorCode)

        val response =
            factory.fail(
                responseCode = errorCode,
                requestData = mapOf("error" to causeMessage),
            )
        return ResponseEntity.status(status).body(response)
    }

    @ExceptionHandler(DopamineException::class)
    fun handleDopamineException(e: DopamineException): ResponseEntity<DopamineResponse<Any?>> {
        logger.warn("[response] DopamineException thrown: {}", e.message)

        val errorCode = CommonErrorCode.BAD_REQUEST
        val status = resolveHttpStatus(errorCode)

        val response = factory.fail(responseCode = errorCode)
        return ResponseEntity.status(status).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<DopamineResponse<Any?>> {
        logger.error("[response] Unhandled exception caught", e)

        val errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR
        val status = resolveHttpStatus(errorCode)

        val response = factory.fail(responseCode = errorCode)
        return ResponseEntity.status(status).body(response)
    }

    private fun resolveHttpStatus(errorCode: ResponseCode): HttpStatus =
        requireNotNull(resolver.resolve(errorCode).httpStatus) {
            "Missing metadata for error code: ${errorCode.code}"
        }
}
