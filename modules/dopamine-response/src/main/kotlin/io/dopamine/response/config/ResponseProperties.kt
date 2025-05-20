package io.dopamine.response.config

import io.dopamine.response.format.TimestampFormat
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpStatus

@ConfigurationProperties(ResponsePropertyKeys.PREFIX)
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
    /**
     * HttpStatus 기반 응답 코드/메시지 커스터마이징 설정
     * - 원하는 메시지로 덮어쓰거나, 내부 코드명을 재정의할 수 있음
     * - 각 항목은 httpStatus 기준으로 매핑됨
     */
    val codes: List<CustomResponseCode> = emptyList(),
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
         * meta.traceId의 key 명칭 (예: traceId, X-Trace-ID 등)
         */
        val traceIdKey: String = "traceId",
        /**
         * 클라이언트 요청에서 traceId 값을 추출할 HTTP 헤더 이름
         */
        val traceIdHeader: String = "X-Trace-ID",
        /**
         * 페이징 정보(paging)를 meta에 포함할지 여부
         * Page<T> 또는 유사 구조일 경우 자동 추출
         */
        val includePaging: Boolean = true,
    )

    /**
     * 설정 파일로 정의할 수 있는 사용자 응답 코드 구조
     */
    data class CustomResponseCode(
        val httpStatus: HttpStatus,
        val code: String,
        val message: String,
    )
}
