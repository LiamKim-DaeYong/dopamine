package io.dopamine.response.core.factory

import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.model.DopamineResponse
import io.dopamine.response.core.trace.TraceContext
import io.dopamine.response.core.trace.TraceIdResolver
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Factory responsible for constructing standardized DopamineResponse<T> instances.
 * Encapsulates formatting, timestamp generation, traceId injection, and response code mapping.
 */
@Component
class DopamineResponseFactory(
    private val props: ResponseProperties,
    private val traceIdResolver: TraceIdResolver,
) {
    fun <T> success(
        data: T?,
        context: TraceContext,
    ): DopamineResponse<T> {
        val timestamp = formatTimestamp()
        val meta = if (props.includeMeta) buildMeta(context) else null
        val (code, message) = resolveCode(HttpStatus.OK)

        return DopamineResponse(
            code = code,
            message = message,
            data = data,
            timestamp = timestamp,
            meta = meta,
        )
    }

    fun <T> of(
        data: T?,
        status: HttpStatus,
        context: TraceContext,
        customCode: String? = null,
        customMessage: String? = null,
        existingMeta: Map<String, Any>? = null,
    ): DopamineResponse<T> {
        val timestamp = formatTimestamp()
        val meta = if (props.includeMeta) mergeMeta(existingMeta, context) else existingMeta
        val (code, message) =
            if (customCode != null && customMessage != null) {
                customCode to customMessage
            } else {
                resolveCode(status)
            }

        return DopamineResponse(
            code = code,
            message = message,
            data = data,
            timestamp = timestamp,
            meta = meta,
        )
    }

    private fun formatTimestamp(): String = LocalDateTime.now().format(props.timestampFormat.formatter())

    private fun buildMeta(context: TraceContext): Map<String, Any> {
        val meta = mutableMapOf<String, Any>()

        if (props.metaOptions.includeTraceId) {
            val key = props.metaOptions.traceIdKey
            val traceId = traceIdResolver.resolve(context)
            if (!traceId.isNullOrBlank()) {
                meta[key] = traceId
            }
        }

        // future: paging, userId, etc.
        return meta
    }

    private fun mergeMeta(
        existing: Map<String, Any>?,
        context: TraceContext,
    ): Map<String, Any> {
        val meta = buildMeta(context).toMutableMap()
        existing?.forEach { (k, v) -> meta.putIfAbsent(k, v) }
        return meta
    }

    private fun resolveCode(status: HttpStatus): Pair<String, String> {
        val match = props.codes.firstOrNull { it.httpStatus == status }
        return if (match != null) {
            match.code to match.message
        } else {
            status.name to status.reasonPhrase
        }
    }
}
