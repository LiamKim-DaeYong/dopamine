package io.dopamine.response.common.metadata

import org.springframework.http.HttpStatus

/**
 * Represents metadata associated with a ResponseCode.
 *
 * This includes HTTP status (if applicable), i18n message key, and fallback default message.
 */
data class ResponseCodeMetadata(
    val code: String,
    val httpStatus: HttpStatus? = null,
    val messageKey: String? = null,
    val defaultMessage: String? = null,
)
