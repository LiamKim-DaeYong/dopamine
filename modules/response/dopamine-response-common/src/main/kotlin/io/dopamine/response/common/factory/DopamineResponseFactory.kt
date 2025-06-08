package io.dopamine.response.common.factory

import io.dopamine.i18n.resolver.MessageResolver
import io.dopamine.response.common.code.DefaultErrorCode
import io.dopamine.response.common.code.DefaultSuccessCode
import io.dopamine.response.common.config.CustomResponseCode
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.model.DopamineResponse
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
    private val messageResolver: MessageResolver,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> success(
        data: T?,
        responseCode: DefaultSuccessCode = DefaultSuccessCode.SUCCESS,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<T> {
        val (code, message) = resolveCode(HttpStatus.valueOf(responseCode.httpStatus))

        return DopamineResponse(
            code = code,
            message = message,
            data = data,
            timestamp = formatTimestamp(),
            meta = meta,
        )
    }

    fun fail(
        responseCode: DefaultErrorCode,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<Nothing> {
        val (code, message) = resolveCode(HttpStatus.valueOf(responseCode.httpStatus))

        return DopamineResponse(
            code = code,
            message = message,
            data = null,
            timestamp = formatTimestamp(),
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

    private fun resolveCode(status: HttpStatus): Pair<String, String> {
        customCodeMap[status.value()]?.let { config ->
            if (config.messageKey != null || config.message != null) {
                messageResolver.resolve(config.messageKey, config.message)?.let { message ->
                    return config.code to message
                }
            }
        }

        DefaultSuccessCode.fromHttpStatus(status)?.let { default ->
            messageResolver.resolve(default.messageKey, default.defaultMessage)?.let { message ->
                return default.code to message
            }
        }

        return status.name to status.reasonPhrase
    }
}
