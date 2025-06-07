package io.dopamine.response.common.code

import io.dopamine.core.code.ResponseCode
import org.springframework.http.HttpStatus

/**
 * Predefined error codes used in HTTP error responses and Spring-based components.
 *
 * Each error includes an HTTP status, internationalization key, and fallback message.
 * Can be extended or overridden with custom ResponseCode implementations.
 */
enum class DefaultErrorCode(
    override val code: String,
    val httpStatus: Int,
    val messageKey: String,
    val defaultMessage: String,
) : ResponseCode {
    // Authentication & Authorization
    UNAUTHORIZED("AUTH_401", 401, "dopamine.error.auth.401", "Authentication is required."),
    FORBIDDEN("AUTH_403", 403, "dopamine.error.auth.403", "Access is forbidden."),
    TOKEN_EXPIRED("AUTH_401_EXPIRED", 401, "dopamine.error.auth.401.expired", "Token has expired."),
    INVALID_TOKEN("AUTH_401_INVALID", 401, "dopamine.error.auth.401.invalid", "Token is invalid."),

    // Validation
    BAD_REQUEST("VALID_400", 400, "dopamine.error.valid.400", "Invalid request."),

    // Domain
    NOT_FOUND("DOMAIN_404", 404, "dopamine.error.domain.404", "The requested resource was not found."),
    CONFLICT("DOMAIN_409", 409, "dopamine.error.domain.409", "Resource already exists."),
    RULE_VIOLATION("DOMAIN_400_RULE", 400, "dopamine.error.domain.400.rule", "Business rule violated."),

    // System
    INTERNAL_SERVER_ERROR("SYS_500", 500, "dopamine.error.sys.500", "Internal server error."),
    SERVICE_UNAVAILABLE("SYS_503", 503, "dopamine.error.sys.503", "Service is temporarily unavailable."),

    // External
    BAD_GATEWAY("EXTERNAL_502", 502, "dopamine.error.external.502", "External system error."),
    GATEWAY_TIMEOUT("EXTERNAL_504", 504, "dopamine.error.external.504", "External system timeout."),
    ;

    companion object {
        fun fromCode(code: String): DefaultErrorCode? = entries.find { it.code == code }

        fun fromHttpStatus(httpStatus: HttpStatus): DefaultErrorCode? =
            entries.find { it.httpStatus == httpStatus.value() }

        fun default(): DefaultErrorCode = INTERNAL_SERVER_ERROR
    }
}
