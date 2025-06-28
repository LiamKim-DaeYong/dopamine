package io.dopamine.response.common.metadata

import io.dopamine.core.code.ResponseCode

/**
 * Provides metadata associated with a [ResponseCode].
 * Used to resolve status, message, and other context-specific metadata dynamically.
 */
interface ResponseCodeRegistry {
    fun get(code: ResponseCode): ResponseCodeMetadata?

    fun get(code: String): ResponseCodeMetadata?

    fun getAll(): List<ResponseCodeMetadata>
}
