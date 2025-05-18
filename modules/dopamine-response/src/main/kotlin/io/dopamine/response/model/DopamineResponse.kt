package io.dopamine.response.model

import io.dopamine.response.code.ResponseCode
import io.dopamine.response.code.SuccessCode
import java.time.LocalDateTime

/**
 * 표준 API 응답 구조
 */
data class DopamineResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null,
    val timestamp: String,
    val meta: Map<String, Any>? = null,
) {
    companion object {
        /**
         * 성공 응답 생성 (기본 SuccessCode.SUCCESS)
         */
        fun <T> success(
            data: T? = null,
            code: SuccessCode = SuccessCode.SUCCESS,
            timestamp: String = LocalDateTime.now().toString(),
            meta: Map<String, Any>? = null,
        ): DopamineResponse<T> =
            DopamineResponse(
                code = code.code,
                message = code.message,
                data = data,
                timestamp = timestamp,
                meta = meta,
            )

        /**
         * 실패 응답 생성 (ResponseCode 기반)
         */
        fun error(
            code: ResponseCode,
            message: String = code.message,
            meta: Map<String, Any>? = null,
            timestamp: String = LocalDateTime.now().toString(),
        ): DopamineResponse<Unit> =
            DopamineResponse(
                code = code.code,
                message = message,
                data = null,
                timestamp = timestamp,
                meta = meta,
            )
    }
}
