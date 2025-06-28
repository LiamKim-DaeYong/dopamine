package io.dopamine.starter.mvc.support

import io.dopamine.core.resolver.TraceIdResolver
import io.dopamine.response.common.metadata.MetaContributor
import io.dopamine.trace.mvc.config.TraceProperties
import io.dopamine.trace.mvc.request.ServletTraceContext
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * A MetaContributor that adds the traceId to the response metadata.
 */
class TraceIdMetaContributor(
    private val traceIdResolver: TraceIdResolver,
    private val traceProperties: TraceProperties,
) : MetaContributor {
    override fun contribute(data: Any?): Map<String, Any> {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request
        val context = request?.let { ServletTraceContext(it) }

        val traceId = context?.let { traceIdResolver.resolve(it) }

        return if (traceId != null) {
            mapOf(traceProperties.traceIdKey to traceId)
        } else {
            emptyMap()
        }
    }
}
