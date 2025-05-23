package io.dopamine.response.mvc.trace

import io.dopamine.response.core.trace.TraceContext
import io.dopamine.response.core.trace.TraceIdResolver
import org.slf4j.MDC

/**
 * TraceIdResolver that extracts traceId from SLF4J MDC.
 * Key is configurable externally via application settings.
 */
class MdcTraceIdResolver(
    private val mdcKey: String,
) : TraceIdResolver {
    init {
        require(mdcKey.isNotBlank()) { "MDC key must not be blank" }
    }

    override fun resolve(context: TraceContext): String? = MDC.get(mdcKey)?.takeIf { it.isNotBlank() }
}
