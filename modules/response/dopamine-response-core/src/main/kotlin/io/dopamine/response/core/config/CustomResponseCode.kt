package io.dopamine.response.core.config

/**
 * Custom response code structure mapped by HTTP status.
 * Allows external configuration of response messages and internal codes.
 *
 * At least one of [message] or [messageKey] must be provided.
 */
data class CustomResponseCode(
    val httpStatus: Int,
    val code: String,
    val message: String? = null,
    val messageKey: String? = null,
) {
    init {
        require(message != null || messageKey != null) {
            "Either 'message' or 'messageKey' must be provided for CustomResponseCode(code=$code, httpStatus=$httpStatus)"
        }
    }
}
