package io.dopamine.response.common.code

import io.dopamine.response.common.metadata.ResponseCodeMetadata
import org.springframework.http.HttpStatus

/**
 * Custom response code structure defined via external configuration.
 * Used to override default response code behavior or introduce new mappings.
 *
 * At least one of [messageKey] or [defaultMessage] must be provided.
 */
data class CustomResponseCode(
    val code: String,
    val httpStatus: Int,
    val messageKey: String? = null,
    val defaultMessage: String? = null,
) {
    init {
        require(messageKey != null || defaultMessage != null) {
            "Either 'messageKey' or 'defaultMessage' must be provided for CustomResponseCode(code=$code, httpStatus=$httpStatus)"
        }
    }

    fun toMetadata(): ResponseCodeMetadata =
        ResponseCodeMetadata(
            code = code,
            httpStatus = HttpStatus.resolve(httpStatus) ?: HttpStatus.INTERNAL_SERVER_ERROR,
            messageKey = messageKey ?: code,
            defaultMessage = defaultMessage,
        )
}
