package io.dopamine.response.common.extractor

import io.dopamine.response.common.model.PagingMeta
import org.springframework.data.domain.Page
import org.springframework.data.domain.Slice

/**
 * Utility to extract [PagingMeta] from Spring [Page] or [Slice] instances.
 */
object PagingMetaExtractor {
    fun from(page: Page<*>): PagingMeta =
        PagingMeta(
            page = page.number,
            size = page.size,
            hasNext = page.hasNext(),
            hasPrevious = page.hasPrevious(),
            isFirst = page.isFirst,
            isLast = page.isLast,
            totalPages = page.totalPages,
            totalElements = page.totalElements,
        )

    fun from(slice: Slice<*>): PagingMeta =
        PagingMeta(
            page = slice.number,
            size = slice.size,
            hasNext = slice.hasNext(),
            hasPrevious = slice.hasPrevious(),
            isFirst = slice.isFirst,
            isLast = slice.isLast,
            totalPages = null,
            totalElements = null,
        )
}
