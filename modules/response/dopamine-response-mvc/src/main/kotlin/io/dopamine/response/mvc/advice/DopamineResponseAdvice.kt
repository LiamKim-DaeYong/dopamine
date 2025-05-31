package io.dopamine.response.mvc.advice

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.response.core.model.DopamineResponse
import io.dopamine.trace.core.resolver.TraceIdResolver
import io.dopamine.trace.mvc.config.TraceProperties
import io.dopamine.trace.mvc.request.ServletTraceContext
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
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
class DopamineResponseAdvice(
    private val factory: DopamineResponseFactory,
    private val traceIdResolver: TraceIdResolver,
    private val traceProperties: TraceProperties,
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
        // Skip reactive types or already wrapped response entities
        if (isReactive(body) || body is ResponseEntity<*>) return body

        val context = ServletTraceContext(request)
        val traceId = traceIdResolver.resolve(context)

        return when (body) {
            is DopamineResponse<*> -> {
                val mergedMeta = mergeMeta(body.meta, traceId)
                body.copy(meta = mergedMeta)
            }

            else -> {
                val meta = buildMeta(traceId)
                val wrapped = factory.success(data = body, meta = meta)

                // Special case: when controller returns a raw String
                if (returnType.parameterType == String::class.java) {
                    objectMapper.writeValueAsString(wrapped)
                } else {
                    wrapped
                }
            }
        }
    }

    private fun buildMeta(traceId: String?): Map<String, Any> {
        if (traceId.isNullOrBlank()) return emptyMap()
        return mapOf(traceProperties.traceIdKey to traceId)
    }

    private fun mergeMeta(
        existing: Map<String, Any>?,
        traceId: String?,
        key: String = traceProperties.traceIdKey,
    ): Map<String, Any> {
        if (traceId.isNullOrBlank()) return existing ?: emptyMap()
        val merged = existing?.toMutableMap() ?: mutableMapOf()
        merged.putIfAbsent(key, traceId)
        return merged
    }

    private fun isReactive(body: Any?): Boolean {
        val className = body?.javaClass?.name ?: return false
        return className.startsWith("reactor.core.publisher.")
    }
}
