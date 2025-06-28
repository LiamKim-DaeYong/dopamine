package io.dopamine.response.common.model

/**
 * Represents pagination metadata extracted from Spring Page or Slice types.
 *
 * This structure is intended to be serialized into `meta.paging` in API responses.
 */
data class PagingMeta(
    val page: Int,
    val size: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val isFirst: Boolean,
    val isLast: Boolean,
    val totalPages: Int? = null,
    val totalElements: Long? = null,
)
