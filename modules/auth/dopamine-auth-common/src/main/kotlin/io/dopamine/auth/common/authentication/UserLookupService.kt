package io.dopamine.auth.common.authentication

/**
 * Provides a way to resolve a [UserPrincipal] based on identity information
 * such as username or subject from the authentication token.
 */
fun interface UserLookupService {
    fun load(identity: String): UserPrincipal?
}
