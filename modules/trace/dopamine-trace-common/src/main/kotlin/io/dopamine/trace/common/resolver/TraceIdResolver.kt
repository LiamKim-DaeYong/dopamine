package io.dopamine.trace.common.resolver

import io.dopamine.trace.common.request.RequestTraceContext

/**
 * Strategy interface for extracting traceId from a request context.
 * - Designed to support multiple environments like Servlet or WebFlux
 */
fun interface TraceIdResolver {
    fun resolve(context: RequestTraceContext): String?
}
