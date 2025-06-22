package io.dopamine.auth.common.authentication

/**
 * Provides a way to store and manage authentication tokens
 * when using stateful strategies (e.g., refresh tokens, blacklist).
 */
interface TokenStore {
    /**
     * Persists a token for future validation.
     */
    fun save(token: Token)

    /**
     * Checks if the token is currently valid (e.g., not revoked).
     */
    fun isValid(token: Token): Boolean

    /**
     * Invalidates a token, typically for logout or revocation.
     */
    fun invalidate(token: Token)
}
