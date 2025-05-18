package io.dopamine.response.config

import io.dopamine.response.format.TimestampFormat
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("dopamine.response")
data class ResponseProperties(
    /**
     * DopamineResponse 자동 래핑 기능 활성화 여부
     * false일 경우 모든 API 응답은 원형 그대로 반환됨
     */
    val enabled: Boolean = true,
    /**
     * meta 필드 포함 여부 (traceId, paging 등)
     * false이면 모든 meta 데이터는 응답에서 제외됨
     */
    val includeMeta: Boolean = true,
    /**
     * meta 필드 내 세부 항목 설정 (traceId, paging 등)
     * includeMeta = false인 경우 무시됨
     */
    val metaOptions: MetaOptions = MetaOptions(),
    /**
     * 응답 timestamp 포맷 설정 (ISO_8601, DATETIME, COMPACT 등)
     */
    val timestampFormat: TimestampFormat = TimestampFormat.ISO_8601,
) {
    /**
     * meta 필드 세부 항목 설정 클래스
     */
    data class MetaOptions(
        /**
         * traceId 값을 meta에 포함할지 여부
         */
        val includeTraceId: Boolean = true,
        /**
         * 페이징 정보(paging)를 meta에 포함할지 여부
         * Page<T> 또는 유사 구조일 경우 자동 추출
         */
        val includePaging: Boolean = true,
    )
}
