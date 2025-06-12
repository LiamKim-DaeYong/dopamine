package io.dopamine.response.mvc.advice

import io.dopamine.response.common.code.DefaultErrorCode
import io.dopamine.response.common.exception.DopamineException
import io.dopamine.response.common.factory.DopamineResponseFactory
import io.dopamine.response.common.model.DopamineResponse
import io.dopamine.response.mvc.meta.ResponseMetaBuilder
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class DopamineErrorResponseAdvice(
    private val factory: DopamineResponseFactory,
    private val metaBuilder: ResponseMetaBuilder,
) {
    @ExceptionHandler(DopamineException::class)
    fun handleDopamineException(e: DopamineException): ResponseEntity<DopamineResponse<Nothing>> {
        val meta = metaBuilder.build()
        val errorCode = DefaultErrorCode.BAD_REQUEST

        val response =
            factory.fail(
                responseCode = errorCode,
                meta = meta,
            )
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<DopamineResponse<Nothing>> {
        val meta = metaBuilder.build()
        val errorCode = DefaultErrorCode.INTERNAL_SERVER_ERROR
        val response =
            factory.fail(
                responseCode = errorCode,
                meta = meta,
            )

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(response)
    }
}
