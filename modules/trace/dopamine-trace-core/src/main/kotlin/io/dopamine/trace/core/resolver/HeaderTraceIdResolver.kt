package io.dopamine.trace.core.resolver

import io.dopamine.trace.core.request.RequestTraceContext

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

    override fun resolve(context: RequestTraceContext): String? = context.getHeader(headerName)?.takeIf { it.isNotBlank() }
}
