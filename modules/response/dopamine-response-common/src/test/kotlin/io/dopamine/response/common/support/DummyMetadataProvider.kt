package io.dopamine.response.common.support

import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.metadata.ResponseCodeMetadata
import io.dopamine.response.common.metadata.ResponseMetadataProvider

/**
 * A test implementation of [ResponseMetadataProvider] that returns predefined metadata.
 */
class DummyMetadataProvider(
    predefined: List<ResponseCodeMetadata> = emptyList(),
) : ResponseMetadataProvider {
    private val map: Map<String, ResponseCodeMetadata> = predefined.associateBy { it.code }

    override fun supports(code: ResponseCode): Boolean = map.containsKey(code.code)

    override fun provide(code: ResponseCode): ResponseCodeMetadata =
        map[code.code]
            ?: throw IllegalArgumentException("No metadata for code: ${code.code}")
}
