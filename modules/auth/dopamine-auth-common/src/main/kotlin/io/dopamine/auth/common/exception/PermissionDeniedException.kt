package io.dopamine.auth.common.exception

/**
 * Thrown when the authenticated user does not have permission to access a resource.
 */
class PermissionDeniedException(
    message: String,
    cause: Throwable? = null,
) : AuthException(message, cause)
