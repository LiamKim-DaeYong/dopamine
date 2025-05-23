package io.dopamine.response.core.trace

/**
 * Abstract interface for extracting values from a request context.
 *
 * This context abstraction allows traceId resolvers to work with various types of web requests,
 * such as HttpServletRequest (MVC), ServerWebExchange (WebFlux), or other environments.
 */
interface TraceContext {
    fun getHeader(name: String): String?

    fun getAttribute(name: String): String?
}
