package io.dopamine.response.core.format

import java.time.format.DateTimeFormatter

/**
 * Supported timestamp formats for response metadata.
 */
enum class TimestampFormat(
    private val pattern: String,
) {
    /**
     * ISO 8601 standard format: 2025-05-17T16:30:00
     */
    ISO_8601("yyyy-MM-dd'T'HH:mm:ss"),

    /**
     * ISO 8601 with milliseconds: 2025-05-17T16:30:00.123
     */
    ISO_8601_MILLIS("yyyy-MM-dd'T'HH:mm:ss.SSS"),

    /**
     * Common datetime format: 2025-05-17 16:30:00
     */
    DATETIME("yyyy-MM-dd HH:mm:ss"),

    /**
     * Date only: 2025-05-17
     */
    DATE("yyyy-MM-dd"),

    /**
     * Compact format, useful for IDs or filenames: 20250517T163000
     */
    COMPACT("yyyyMMdd'T'HHmmss"),
    ;

    fun formatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
}
