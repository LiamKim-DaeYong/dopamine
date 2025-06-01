package io.dopamine.response.mvc.advice

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.response.core.model.DopamineResponse
import io.dopamine.response.mvc.meta.ResponseMetaBuilder
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * Intercepts all REST controller responses and wraps them in a standardized [DopamineResponse] format.
 * If traceId is available, it is added to the meta field (if not already present).
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class DopamineResponseAdvice(
    private val factory: DopamineResponseFactory,
    private val objectMapper: ObjectMapper,
    private val metaBuilder: ResponseMetaBuilder,
) : ResponseBodyAdvice<Any> {
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        val clazz = returnType.parameterType
        return !DopamineResponse::class.java.isAssignableFrom(clazz) &&
            !ResponseEntity::class.java.isAssignableFrom(clazz) &&
            !clazz.name.startsWith("reactor.core.publisher.")
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        contentType: MediaType,
        converterType: Class<out HttpMessageConverter<*>>,
        req: ServerHttpRequest,
        res: ServerHttpResponse,
    ): Any? {
        // Skip reactive types or already wrapped response entities
        if (isReactive(body) || body is ResponseEntity<*>) return body

        return when (body) {
            is DopamineResponse<*> -> {
                body.copy(meta = metaBuilder.merge(body.meta))
            }

            else -> {
                val wrapped = factory.success(data = body, meta = metaBuilder.build())

                // Special case: when controller returns a raw String
                if (returnType.parameterType == String::class.java) {
                    objectMapper.writeValueAsString(wrapped)
                } else {
                    wrapped
                }
            }
        }
    }

    private fun isReactive(body: Any?): Boolean {
        val className = body?.javaClass?.name ?: return false
        return className.startsWith("reactor.core.publisher.")
    }
}
