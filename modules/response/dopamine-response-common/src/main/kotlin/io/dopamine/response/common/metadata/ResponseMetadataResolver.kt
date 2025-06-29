package io.dopamine.response.common.metadata

import io.dopamine.core.code.ResponseCode

/**
 * Resolves metadata for a given [ResponseCode].
 */
interface ResponseMetadataResolver {
    fun resolve(code: ResponseCode): ResponseCodeMetadata
}
