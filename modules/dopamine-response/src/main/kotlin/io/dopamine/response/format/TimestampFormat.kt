package io.dopamine.response.format

import java.time.format.DateTimeFormatter

/**
 * 응답 타임스탬프 포맷 옵션
 */
enum class TimestampFormat(private val pattern: String) {
    /**
     * ISO 표준 포맷: 2025-05-17T16:30:00
     */
    ISO_8601("yyyy-MM-dd'T'HH:mm:ss"),

    /**
     * 일반적인 날짜-시간 포맷: 2025-05-17 16:30:00
     */
    DATETIME("yyyy-MM-dd HH:mm:ss"),

    /**
     * 날짜만: 2025-05-17
     */
    DATE("yyyy-MM-dd"),

    /**
     * 밀집된 형태, 파일명/ID 등에 적합: 20250517T163000
     */
    COMPACT("yyyyMMdd'T'HHmmss"),
    ;

    fun formatter(): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
}
