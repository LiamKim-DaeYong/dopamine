package io.dopamine.auth.common.authentication

/**
 * Provides operations for issuing, parsing, and validating authentication tokens.
 */
interface TokenProvider {
    /**
     * Issues a new authentication token with the given payload.
     */
    fun generate(payload: Map<String, Any>): Token

    /**
     * Parses a raw token string into a [Token] object.
     */
    fun parse(rawToken: String): Token

    /**
     * Validates whether the given token is valid (e.g., not expired or malformed).
     */
    fun validate(token: Token): Boolean
}
