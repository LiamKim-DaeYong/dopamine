package io.dopamine.response.common.metadata

import io.dopamine.response.common.extractor.PagingMetaExtractor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Slice

/**
 * Contributes pagination metadata to the response if the response data is a Page or Slice.
 *
 * This contributor respects the includePaging flag and will skip contribution if disabled.
 */
class PagingMetaContributor(
    private val includePaging: Boolean,
) : MetaContributor {
    override fun contribute(data: Any?): Map<String, Any> {
        if (!includePaging || data == null) return emptyMap()

        return when (data) {
            is Page<*> -> mapOf("paging" to PagingMetaExtractor.from(data))
            is Slice<*> -> mapOf("paging" to PagingMetaExtractor.from(data))
            else -> emptyMap()
        }
    }
}
