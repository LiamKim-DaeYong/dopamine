package io.dopamine.response.core.trace

/**
 * Strategy interface for extracting traceId from a request context.
 * - Designed to support multiple environments like Servlet or WebFlux
 */
fun interface TraceIdResolver {
    fun resolve(context: TraceContext): String?
}
