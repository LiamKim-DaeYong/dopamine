package io.dopamine.response.exception

import io.dopamine.response.code.ErrorCode

object ExceptionFactory {
    /**
     * 인증/인가 관련 예외
     */
    object Auth {
        fun unauthorized(message: String? = null): DopamineException =
            DopamineException(ErrorCode.UNAUTHORIZED, message ?: ErrorCode.UNAUTHORIZED.message)

        fun forbidden(message: String? = null): DopamineException =
            DopamineException(ErrorCode.FORBIDDEN, message ?: ErrorCode.FORBIDDEN.message)

        fun tokenExpired(): DopamineException = DopamineException(ErrorCode.TOKEN_EXPIRED)

        fun invalidToken(): DopamineException = DopamineException(ErrorCode.INVALID_TOKEN)
    }

    /**
     * 입력값/요청값 유효성 검증 실패
     */
    object Validation {
        fun badRequest(message: String? = null): DopamineException =
            DopamineException(ErrorCode.VALIDATION_FAILED, message ?: "잘못된 요청입니다.")
    }

    /**
     * 비즈니스 도메인 예외
     */
    object Domain {
        fun notFound(message: String? = null): DopamineException =
            DopamineException(ErrorCode.RESOURCE_NOT_FOUND, message ?: "요청한 리소스를 찾을 수 없습니다.")

        fun conflict(message: String? = null): DopamineException =
            DopamineException(ErrorCode.CONFLICT, message ?: "이미 존재하는 리소스입니다.")

        fun ruleViolated(message: String): DopamineException =
            DopamineException(ErrorCode.BUSINESS_RULE_VIOLATION, message)
    }

    /**
     * 시스템 내부 장애 (Infra, 처리 실패 등)
     */
    object System {
        fun internal(
            message: String? = null,
            cause: Throwable? = null,
        ): DopamineException =
            DopamineException(ErrorCode.INTERNAL_ERROR, message ?: ErrorCode.INTERNAL_ERROR.message, cause)

        fun serviceUnavailable(message: String? = null): DopamineException =
            DopamineException(ErrorCode.SERVICE_UNAVAILABLE, message ?: "서비스가 일시적으로 불가능합니다.")
    }

    /**
     * 외부 시스템 / 외부 API 연동 예외
     */
    object External {
        fun gatewayTimeout(
            message: String? = null,
            cause: Throwable? = null,
        ): DopamineException = DopamineException(ErrorCode.GATEWAY_TIMEOUT, message ?: "외부 시스템 응답 지연", cause)

        fun badGateway(
            message: String? = null,
            cause: Throwable? = null,
        ): DopamineException = DopamineException(ErrorCode.BAD_GATEWAY, message ?: "외부 시스템 오류", cause)

        fun serviceUnavailable(message: String? = null): DopamineException =
            DopamineException(ErrorCode.SERVICE_UNAVAILABLE, message ?: "외부 서비스 사용 불가")
    }
}
