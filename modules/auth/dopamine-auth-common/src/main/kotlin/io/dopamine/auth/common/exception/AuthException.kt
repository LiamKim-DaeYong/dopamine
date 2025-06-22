package io.dopamine.auth.common.exception

/**
 * Base class for all authentication and authorization related exceptions.
 */
open class AuthException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
