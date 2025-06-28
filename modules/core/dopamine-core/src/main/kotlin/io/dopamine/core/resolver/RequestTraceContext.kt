package io.dopamine.core.resolver

/**
 * Provides access to request-level metadata such as headers or attributes,
 * used for resolving an incoming traceId.
 *
 * Typical implementations may wrap HttpServletRequest or ServerWebExchange.
 */
interface RequestTraceContext {
    fun getHeader(name: String): String?

    fun getAttribute(name: String): String?
}
