package io.dopamine.response.common.exception

/**
 * Unified exception type for all Dopamine-based applications.
 *
 * This exception encapsulates a response code and a human-readable message,
 * and can optionally wrap a cause (nested exception).
 *
 * It is used to generate standardized API error responses and supports
 * external message resolution (e.g., via message source or configuration).
 */
class DopamineException(
    val code: String,
    val messageKey: String? = null,
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)
