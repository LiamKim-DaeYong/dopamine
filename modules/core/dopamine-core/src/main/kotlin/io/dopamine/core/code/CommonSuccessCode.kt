package io.dopamine.core.code

/**
 * Common success response codes used across services and modules.
 *
 * These codes are transport-agnostic identifiers and should be interpreted
 * through metadata such as HTTP status and localized messages, which are
 * resolved externally via registries and resolvers.
 */
enum class CommonSuccessCode(
    override val code: String,
) : ResponseCode {
    SUCCESS("SUCCESS"),
    CREATED("CREATED"),
    ACCEPTED("ACCEPTED"),
    NO_CONTENT("NO_CONTENT"),
}
