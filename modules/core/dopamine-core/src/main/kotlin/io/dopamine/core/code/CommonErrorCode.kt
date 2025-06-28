package io.dopamine.core.code

/**
 * Common error response codes used across services and modules.
 *
 * These codes are transport-agnostic identifiers and should be interpreted
 * through metadata such as HTTP status and localized messages, which are
 * resolved externally via registries and resolvers.
 */
enum class CommonErrorCode(
    override val code: String,
) : ResponseCode {
    // fallback (generic 400)
    BAD_REQUEST("BAD_REQUEST"),

    // validation (400)
    INVALID_REQUEST("INVALID_REQUEST"),

    // domain logic (404, 409, 400.rule)
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    RESOURCE_ALREADY_EXISTS("RESOURCE_ALREADY_EXISTS"),
    BUSINESS_RULE_VIOLATION("BUSINESS_RULE_VIOLATION"),

    // external system
    EXTERNAL_TIMEOUT("EXTERNAL_TIMEOUT"),
    EXTERNAL_ERROR("EXTERNAL_ERROR"),

    // internal/system-level errors (5xx)
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE"),
}
