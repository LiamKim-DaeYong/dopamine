package io.dopamine.response.code

import org.springframework.http.HttpStatus

/**
 * 공통 실패 응답 코드
 */
enum class ErrorCode(
    override val code: String,
    override val message: String,
    override val httpStatus: HttpStatus,
) : ResponseCode {
    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // Validation
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청이 유효하지 않습니다."),

    // Domain
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),
    CONFLICT(HttpStatus.CONFLICT),
    BUSINESS_RULE_VIOLATION(HttpStatus.BAD_REQUEST),

    // System
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE),

    // External
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY),
    ;

    constructor(httpStatus: HttpStatus) : this(
        code = httpStatus.name,
        message = httpStatus.reasonPhrase,
        httpStatus = httpStatus,
    )

    constructor(httpStatus: HttpStatus, message: String) : this(
        code = httpStatus.name,
        message = message,
        httpStatus = httpStatus,
    )
}
