package io.dopamine.trace.common.resolver

import io.dopamine.core.resolver.RequestTraceContext
import io.dopamine.core.resolver.TraceIdResolver

/**
 * TraceIdResolver that extracts traceId from the request header via TraceContext.
 * Header name is externally configurable via application settings.
 */
class HeaderTraceIdResolver(
    private val headerName: String,
) : TraceIdResolver {
    init {
        require(headerName.isNotBlank()) { "headerName must not be blank" }
    }

    override fun resolve(context: RequestTraceContext): String? =
        context.getHeader(headerName)?.takeIf { it.isNotBlank() }
}
