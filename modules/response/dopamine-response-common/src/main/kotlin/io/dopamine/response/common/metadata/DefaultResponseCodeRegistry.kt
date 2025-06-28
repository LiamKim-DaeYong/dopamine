package io.dopamine.response.common.metadata

import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.code.CustomResponseCode

class DefaultResponseCodeRegistry(
    predefined: List<ResponseCodeMetadata>,
    custom: List<CustomResponseCode>,
) : ResponseCodeRegistry {
    private val metadataMap: Map<String, ResponseCodeMetadata> =
        buildMap {
            predefined.forEach { put(it.code, it) }
            custom.forEach { put(it.code, it.toMetadata()) }
        }

    override fun get(code: ResponseCode): ResponseCodeMetadata? = metadataMap[code.code]

    override fun get(code: String): ResponseCodeMetadata? = metadataMap[code]

    override fun getAll(): List<ResponseCodeMetadata> = metadataMap.values.toList()
}
