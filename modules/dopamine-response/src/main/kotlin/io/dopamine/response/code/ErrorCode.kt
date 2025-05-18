package io.dopamine.response.code

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus

/**
 * 공통 실패 응답 코드
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    override val code: String,
    override val message: String,
    override val httpStatus: HttpStatus,
) : ResponseCode {
    // Auth
    UNAUTHORIZED("AUTH_401", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("AUTH_403", "금지된 요청입니다.", HttpStatus.FORBIDDEN),
    ACCESS_DENIED("AUTH_ACCESS_DENIED", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED("AUTH_TOKEN_EXPIRED", "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("AUTH_INVALID_TOKEN", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),

    // VALIDATION
    VALIDATION_FAILED("VALID_400", "요청이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // DOMAIN
    RESOURCE_NOT_FOUND("DOMAIN_404", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("DOMAIN_405", "허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    CONFLICT("DOMAIN_409", "이미 존재하는 리소스입니다.", HttpStatus.CONFLICT),
    BUSINESS_RULE_VIOLATION("DOMAIN_400_RULE", "비즈니스 규칙에 위반되었습니다.", HttpStatus.BAD_REQUEST),

    // SYSTEM
    INTERNAL_ERROR("SYS_500", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("SYS_503", "서비스를 사용할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE),

    // EXTERNAL
    GATEWAY_TIMEOUT("EXTERNAL_504", "외부 시스템 응답 지연", HttpStatus.GATEWAY_TIMEOUT),
    BAD_GATEWAY("EXTERNAL_502", "외부 시스템 오류", HttpStatus.BAD_GATEWAY),
    ;

    companion object {
        fun from(status: HttpStatus): ErrorCode? = entries.find { it.httpStatus == status }
    }
}
