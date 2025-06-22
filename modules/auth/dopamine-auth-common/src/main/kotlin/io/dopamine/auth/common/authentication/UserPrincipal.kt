package io.dopamine.auth.common.authentication

/**
 * Represents an authenticated user's identity and roles.
 */
interface UserPrincipal {
    val id: String
    val username: String
    val roles: List<String>

    /**
     * Optional attributes for additional claims.
     */
    val attributes: Map<String, Any>
        get() = emptyMap()
}
