package io.dopamine.auth.common.authorization

import java.lang.reflect.Method

/**
 * Extracts the target resource ID for authorization checks.
 */
fun interface PermissionContext {
    /**
     * Resolves the target resource ID (e.g., orderId) from the method arguments.
     *
     * @param method the method being invoked
     * @param args the actual argument values of the method
     * @return the ID of the resource, or null if not found
     */
    fun resolveTargetId(
        method: Method,
        args: Array<Any?>,
    ): Any?
}
