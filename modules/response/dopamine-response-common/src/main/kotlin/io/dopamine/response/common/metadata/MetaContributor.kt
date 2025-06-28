package io.dopamine.response.common.metadata

/**
 * Provides additional metadata to be injected into DopamineResponse.
 */
fun interface MetaContributor {
    fun contribute(data: Any?): Map<String, Any>
}
