package io.dopamine.response.core.trace

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

    override fun resolve(context: TraceContext): String? =
        resolvers
            .asSequence()
            .map { it.resolve(context) }
            .firstOrNull { !it.isNullOrBlank() }
}
