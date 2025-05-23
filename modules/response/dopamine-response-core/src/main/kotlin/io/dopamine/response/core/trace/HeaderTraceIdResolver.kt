package io.dopamine.response.core.trace

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

    override fun resolve(context: TraceContext): String? = context.getHeader(headerName)?.takeIf { it.isNotBlank() }
}
