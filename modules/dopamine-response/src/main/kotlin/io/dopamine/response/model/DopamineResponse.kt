package io.dopamine.response.model

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
        fun <T> success(
            data: T? = null,
            code: SuccessCode = SuccessCode.SUCCESS,
            timestamp: String = LocalDateTime.now().toString(),
            meta: Map<String, Any>? = null,
        ): DopamineResponse<T> {
            return DopamineResponse(
                code = code.code,
                message = code.message,
                data = data,
                timestamp = timestamp,
                meta = meta,
            )
        }
    }
}
