package io.dopamine.response.common.metadata

import io.dopamine.core.code.ResponseCode

/**
 * Default implementation of [ResponseMetadataResolver] using a list of [ResponseMetadataProvider]s.
 */
class DefaultResponseMetadataResolver(
    private val providers: List<ResponseMetadataProvider>,
) : ResponseMetadataResolver {
    override fun resolve(code: ResponseCode): ResponseCodeMetadata =
        providers
            .firstOrNull { it.supports(code) }
            ?.provide(code)
            ?: throw IllegalStateException("No metadata found for code: ${code.code}")
}
