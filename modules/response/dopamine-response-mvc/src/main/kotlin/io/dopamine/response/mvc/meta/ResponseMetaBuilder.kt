package io.dopamine.response.mvc.meta

import io.dopamine.trace.core.resolver.TraceIdResolver
import io.dopamine.trace.mvc.config.TraceProperties
import io.dopamine.trace.mvc.request.ServletTraceContext
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

/**
 * Builds the meta section for DopamineResponse,
 * including traceId and other contextual data if needed.
 */
@Component
class ResponseMetaBuilder(
    private val traceIdResolver: TraceIdResolver,
    private val traceProperties: TraceProperties,
    private val request: HttpServletRequest,
) {
    fun build(): Map<String, Any> {
        val traceId = resolveTraceId()
        return if (traceId.isNullOrBlank()) {
            emptyMap()
        } else {
            mapOf(traceProperties.traceIdKey to traceId)
        }
    }

    fun merge(existing: Map<String, Any>?): Map<String, Any> {
        val traceId = resolveTraceId()
        if (traceId.isNullOrBlank()) return existing ?: emptyMap()

        val merged = existing?.toMutableMap() ?: mutableMapOf()
        merged.putIfAbsent(traceProperties.traceIdKey, traceId)
        return merged
    }

    private fun resolveTraceId(): String? {
        val context = ServletTraceContext(request)
        return traceIdResolver.resolve(context)
    }
}
