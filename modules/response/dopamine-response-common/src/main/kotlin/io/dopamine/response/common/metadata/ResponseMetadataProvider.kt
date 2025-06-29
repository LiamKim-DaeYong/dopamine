package io.dopamine.response.common.metadata

import io.dopamine.core.code.ResponseCode

/**
 * Provides metadata for a specific [ResponseCode].
 */
interface ResponseMetadataProvider {
    fun supports(code: ResponseCode): Boolean

    fun provide(code: ResponseCode): ResponseCodeMetadata
}
