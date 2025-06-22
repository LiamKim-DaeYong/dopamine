package io.dopamine.auth.common.exception

/**
 * Thrown when the authentication token is missing, malformed, or invalid.
 */
class InvalidTokenException(
    message: String,
    cause: Throwable? = null,
) : AuthException(message, cause)
