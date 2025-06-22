package io.dopamine.auth.mvc.internal.user

import io.dopamine.auth.common.authentication.UserLookupService
import io.dopamine.auth.common.authentication.UserPrincipal

/**
 * Default in-memory implementation of [UserLookupService].
 * Always returns a fixed test user.
 */
class InMemoryUserLookupService : UserLookupService {
    override fun load(identity: String): UserPrincipal? =
        object : UserPrincipal {
            override val id: String = identity
            override val username: String = "test-$identity"
            override val roles: List<String> = listOf("ROLE_USER")
            override val attributes: Map<String, Any> = emptyMap()
        }
}
