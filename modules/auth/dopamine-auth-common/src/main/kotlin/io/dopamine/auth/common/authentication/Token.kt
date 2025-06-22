package io.dopamine.auth.common.authentication

import java.time.Instant

/**
 * Represents a parsed authentication token.
 */
interface Token {
    val value: String
    val subject: String
    val issuedAt: Instant
    val expiresAt: Instant
    val claims: Map<String, Any>
}
