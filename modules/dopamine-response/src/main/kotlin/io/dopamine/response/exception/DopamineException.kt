package io.dopamine.response.exception

import io.dopamine.response.code.ResponseCode

/**
 * Dopamine 기반 공통 예외 클래스
 *
 * 응답 포맷(ResponseCode 기반)을 위한 RuntimeException
 * - ErrorCode를 포함하고
 * - message override 및 원인 예외(cause)도 함께 처리 가능
 */
open class DopamineException(
    val responseCode: ResponseCode,
    override val message: String = responseCode.message,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)
