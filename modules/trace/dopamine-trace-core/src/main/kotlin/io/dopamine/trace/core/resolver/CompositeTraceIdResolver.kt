package io.dopamine.trace.core.resolver

import io.dopamine.trace.core.request.RequestTraceContext

/**
 * Composite TraceIdResolver that attempts multiple strategies in order.
 * The first non-blank traceId found will be returned.
 */
class CompositeTraceIdResolver(
    private val resolvers: List<TraceIdResolver>,
) : TraceIdResolver {
    init {
        require(resolvers.isNotEmpty()) { "At least one TraceIdResolver must be provided." }
    }

    override fun resolve(context: RequestTraceContext): String? =
        resolvers
            .asSequence()
            .map { it.resolve(context) }
            .firstOrNull { !it.isNullOrBlank() }
}
