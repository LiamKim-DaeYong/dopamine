package io.dopamine.core.code

/**
 * Default error code definitions used across the application.
 * These can be overridden by external configuration.
 */
enum class ErrorCode(
    val code: String,
    val defaultMessage: String,
    val messageKey: String,
) {
    // Authentication & Authorization
    UNAUTHORIZED("AUTH_401", "Authentication is required.", "dopamine.error.auth.401"),
    FORBIDDEN("AUTH_403", "Access is forbidden.", "dopamine.error.auth.403"),
    INVALID_TOKEN("AUTH_401_INVALID", "The token is invalid.", "dopamine.error.auth.401.invalid"),
    TOKEN_EXPIRED("AUTH_401_EXPIRED", "The token has expired.", "dopamine.error.auth.401.expired"),

    // Validation
    VALIDATION_FAILED("VALID_400", "The request is invalid.", "dopamine.error.valid.400"),

    // Domain
    RESOURCE_NOT_FOUND("DOMAIN_404", "The requested resource was not found.", "dopamine.error.domain.404"),
    CONFLICT("DOMAIN_409", "The resource already exists.", "dopamine.error.domain.409"),
    BUSINESS_RULE_VIOLATION("DOMAIN_400_RULE", "Business rule violated.", "dopamine.error.domain.400.rule"),

    // System
    INTERNAL_ERROR("SYS_500", "Internal server error.", "dopamine.error.sys.500"),
    SERVICE_UNAVAILABLE("SYS_503", "Service is temporarily unavailable.", "dopamine.error.sys.503"),

    // External
    GATEWAY_TIMEOUT("EXTERNAL_504", "External system timeout.", "dopamine.error.external.504"),
    BAD_GATEWAY("EXTERNAL_502", "External system error.", "dopamine.error.external.502"),
    ;

    companion object {
        fun fromCode(code: String): ErrorCode? = entries.find { it.code == code }
    }
}
