package io.dopamine.trace.mvc.resolver

import io.dopamine.trace.common.request.RequestTraceContext
import io.dopamine.trace.common.resolver.TraceIdResolver
import org.slf4j.MDC

/**
 * [TraceIdResolver] implementation that extracts the traceId from SLF4J MDC.
 *
 * The MDC key is externally configurable and typically populated by a servlet filter.
 */
class MdcTraceIdResolver(
    private val mdcKey: String,
) : TraceIdResolver {
    init {
        require(mdcKey.isNotBlank()) { "MDC key must not be blank" }
    }

    override fun resolve(context: RequestTraceContext): String? = MDC.get(mdcKey)?.takeIf { it.isNotBlank() }
}
