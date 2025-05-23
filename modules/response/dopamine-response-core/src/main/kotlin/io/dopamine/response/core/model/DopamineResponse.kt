package io.dopamine.response.core.model

/**
 * Standardized API response wrapper.
 *
 * This model encapsulates the structure of all successful or failed responses
 * across Dopamine-based services. It is intended to provide a unified response
 *
 * @param T The type of the actual data payload.
 *
 * @property code A string representing the business or error code (e.g., SUCCESS_200, AUTH_401).
 * @property message A human-readable message explaining the result.
 * @property data Optional data payload. Null if no content is returned (e.g., errors or 204).
 * @property timestamp ISO-formatted timestamp of when the response was generated.
 * @property meta Optional metadata (e.g., traceId, pagination info, etc.).
 */
data class DopamineResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null,
    val timestamp: String,
    val meta: Map<String, Any>? = null,
)
