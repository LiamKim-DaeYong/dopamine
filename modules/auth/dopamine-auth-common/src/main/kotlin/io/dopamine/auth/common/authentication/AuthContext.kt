package io.dopamine.auth.common.authentication

/**
 * Provides access to authentication information for the current request.
 */
interface AuthContext {
    /**
     * The authenticated user.
     */
    val principal: UserPrincipal

    /**
     * The authentication token used in the request.
     */
    val token: Token
}
