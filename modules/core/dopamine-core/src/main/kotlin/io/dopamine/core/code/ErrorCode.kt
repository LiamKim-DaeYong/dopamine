package io.dopamine.core.code

/**
 * Default error code definitions used across the application.
 * These can be overridden by external configuration.
 */
enum class ErrorCode(
    val code: String,
    val defaultMessage: String,
) {
    // Authentication & Authorization
    UNAUTHORIZED("AUTH_401", "Authentication is required."),
    FORBIDDEN("AUTH_403", "Access is forbidden."),
    INVALID_TOKEN("AUTH_401_INVALID", "The token is invalid."),
    TOKEN_EXPIRED("AUTH_401_EXPIRED", "The token has expired."),

    // Validation
    VALIDATION_FAILED("VALID_400", "The request is invalid."),

    // Domain
    RESOURCE_NOT_FOUND("DOMAIN_404", "The requested resource was not found."),
    CONFLICT("DOMAIN_409", "The resource already exists."),
    BUSINESS_RULE_VIOLATION("DOMAIN_400_RULE", "Business rule violated."),

    // System
    INTERNAL_ERROR("SYS_500", "Internal server error."),
    SERVICE_UNAVAILABLE("SYS_503", "Service is temporarily unavailable."),

    // External
    GATEWAY_TIMEOUT("EXTERNAL_504", "External system timeout."),
    BAD_GATEWAY("EXTERNAL_502", "External system error."),
    ;

    companion object {
        fun fromCode(code: String): ErrorCode? = entries.find { it.code == code }
    }
}
