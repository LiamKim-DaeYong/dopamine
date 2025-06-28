package io.dopamine.response.common.support

import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.metadata.ResponseCodeMetadata
import io.dopamine.response.common.metadata.ResponseCodeRegistry

/**
 * A ResponseCodeRegistry that returns nothing.
 * Useful for unit tests that don't depend on registered metadata.
 */
class DummyCodeRegistry(
    private val predefined: List<ResponseCodeMetadata> = emptyList(),
) : ResponseCodeRegistry {
    private val map: Map<String, ResponseCodeMetadata> = predefined.associateBy { it.code }

    override fun get(code: ResponseCode): ResponseCodeMetadata? = map[code.code]

    override fun get(code: String): ResponseCodeMetadata? = map[code]

    override fun getAll(): List<ResponseCodeMetadata> = map.values.toList()
}
