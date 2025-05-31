package io.dopamine.response.core.factory

import io.dopamine.core.code.SuccessCode
import io.dopamine.response.core.code.fromHttpStatus
import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.model.DopamineResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Factory responsible for constructing standardized DopamineResponse<T> instances.
 * Formatting, timestamp generation, and response code mapping are handled here.
 * Optional traceId or meta can be injected externally.
 */

@Component
class DopamineResponseFactory(
    private val props: ResponseProperties,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> success(
        data: T?,
        traceId: String? = null,
    ): DopamineResponse<T> {
        val timestamp = formatTimestamp()
        val meta = if (props.includeMeta) buildMeta(traceId) else null
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
        traceId: String? = null,
        customCode: String? = null,
        customMessage: String? = null,
        existingMeta: Map<String, Any>? = null,
    ): DopamineResponse<T> {
        val timestamp = formatTimestamp()
        val meta = if (props.includeMeta) mergeMeta(existingMeta, traceId) else existingMeta
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

    private fun buildMeta(traceId: String?): Map<String, Any> {
        val meta = mutableMapOf<String, Any>()

        if (props.metaOptions.includeTraceId && !traceId.isNullOrBlank()) {
            val key = props.metaOptions.traceIdKey
            meta[key] = traceId
        }

        // future: paging, userId, etc.
        return meta
    }

    private fun mergeMeta(
        existing: Map<String, Any>?,
        traceId: String?,
    ): Map<String, Any> {
        val meta = buildMeta(traceId).toMutableMap()
        existing?.forEach { (k, v) -> meta.putIfAbsent(k, v) }
        return meta
    }

    private val customCodeMap: Map<Int, Pair<String, String>> by lazy {
        props.codes
            .groupBy { it.httpStatus }
            .mapValues { entry ->
                val duplicates = entry.value
                if (duplicates.size > 1) {
                    logger.warn(
                        "[response] Duplicate CustomResponseCode found for HTTP status {}. Using the first one: {}",
                        entry.key,
                        duplicates.first(),
                    )
                }
                val first = duplicates.first()
                first.code to first.message
            }
    }

    private fun resolveCode(status: HttpStatus): Pair<String, String> {
        customCodeMap[status.value()]?.let { return it }

        SuccessCode.fromHttpStatus(status)?.let {
            return it.code to it.defaultMessage
        }

        return status.name to status.reasonPhrase
    }
}
