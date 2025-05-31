package io.dopamine.response.mvc.advice

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.response.core.model.DopamineResponse
import io.dopamine.trace.core.resolver.TraceIdResolver
import io.dopamine.trace.mvc.request.ServletTraceContext
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * Intercepts all controller responses and wraps them in DopamineResponse<T>.
 * This ensures a consistent response structure across all REST APIs.
 */
@ControllerAdvice
class DopamineResponseAdvice(
    private val factory: DopamineResponseFactory,
    private val traceIdResolver: TraceIdResolver,
    private val objectMapper: ObjectMapper,
    private val request: HttpServletRequest,
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
        // Skip reactive types
        if (isReactive(body)) return body

        // Skip ResponseEntity (already has status/header control)
        if (body is ResponseEntity<*>) return body

        // Build context
        val context = ServletTraceContext(request)
        val traceId = traceIdResolver.resolve(context)

        // Wrap
        val wrapped = factory.success(body, traceId)

        // Handle String return type
        return if (returnType.parameterType == String::class.java) {
            objectMapper.writeValueAsString(wrapped)
        } else {
            wrapped
        }
    }

    private fun isReactive(body: Any?): Boolean {
        val className = body?.javaClass?.name ?: return false
        return className.startsWith("reactor.core.publisher.")
    }
}
