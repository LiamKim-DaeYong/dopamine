package io.dopamine.response.core.factory

import io.dopamine.core.code.SuccessCode
import io.dopamine.i18n.resolver.MessageResolver
import io.dopamine.response.core.code.fromHttpStatus
import io.dopamine.response.core.config.CustomResponseCode
import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.model.DopamineResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Locale

/**
 * Factory responsible for constructing standardized DopamineResponse<T> instances.
 * Formatting, timestamp generation, and response code mapping are handled here.
 * Optional traceId or meta can be injected externally.
 */
@Component
class DopamineResponseFactory(
    private val props: ResponseProperties,
    private val messageResolver: MessageResolver,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> success(
        data: T?,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<T> {
        val timestamp = formatTimestamp()
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
        meta: Map<String, Any>? = null,
        customCode: String? = null,
        customMessage: String? = null,
    ): DopamineResponse<T> {
        val timestamp = formatTimestamp()
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
            meta = if (props.includeMeta) meta else null,
        )
    }

    private fun formatTimestamp(): String = LocalDateTime.now().format(props.timestampFormat.formatter())

    private val customCodeMap: Map<Int, CustomResponseCode> by lazy {
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
                duplicates.first()
            }
    }

    private fun resolveCode(
        status: HttpStatus,
        locale: Locale = Locale.getDefault(),
    ): Pair<String, String> {
        customCodeMap[status.value()]?.let { config ->
            val message =
                config.messageKey?.let { key ->
                    val resolved = messageResolver.resolve(key, locale)
                    if (resolved != key) resolved else null
                } ?: config.message

            val finalMessage =
                message
                    ?: SuccessCode.fromHttpStatusCode(status.value())?.let {
                        val resolved = messageResolver.resolve(it.messageKey, locale)
                        if (resolved != it.messageKey) resolved else it.defaultMessage
                    }
                    ?: status.reasonPhrase

            return config.code to finalMessage
        }

        SuccessCode.fromHttpStatus(status)?.let {
            val resolved = messageResolver.resolve(it.messageKey, locale)
            val message = if (resolved != it.messageKey) resolved else it.defaultMessage
            return it.code to message
        }

        return status.name to status.reasonPhrase
    }
}
