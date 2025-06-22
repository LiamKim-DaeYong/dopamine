package io.dopamine.auth.common.authorization

import io.dopamine.auth.common.authentication.UserPrincipal

/**
 * Determines whether a user has permission to perform a specific action on a resource.
 */
fun interface PermissionProvider {
    /**
     * @param user the authenticated user
     * @param resource the target resource name (e.g., "ORDER", "PRODUCT")
     * @param action the requested action (e.g., "READ", "UPDATE")
     * @param targetId optional ID of the specific target (e.g., orderId = 123)
     * @return true if the user is authorized, false otherwise
     */
    fun isAuthorized(
        user: UserPrincipal,
        resource: String,
        action: String,
        targetId: Any?,
    ): Boolean
}
