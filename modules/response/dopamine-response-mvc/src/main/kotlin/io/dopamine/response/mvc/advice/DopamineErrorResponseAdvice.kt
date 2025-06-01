package io.dopamine.response.mvc.advice

import io.dopamine.response.core.exception.DopamineException
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.response.core.model.DopamineResponse
import io.dopamine.response.mvc.meta.ResponseMetaBuilder
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
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
        val response =
            factory.of(
                data = null,
                status = HttpStatus.BAD_REQUEST,
                customCode = e.code,
                customMessage = e.message,
                meta = meta,
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<DopamineResponse<Nothing>> {
        val meta = metaBuilder.build()
        val response =
            factory.of(
                data = null,
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                meta = meta,
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
}
