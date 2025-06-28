package io.dopamine.response.common.support

import io.dopamine.response.common.metadata.MetaContributor

/**
 * A MetaContributor that always returns predefined static metadata.
 * Useful for testing deterministic response structures.
 */
class StaticMetaContributor(
    private val meta: Map<String, Any>,
) : MetaContributor {
    override fun contribute(data: Any?): Map<String, Any> = meta
}
