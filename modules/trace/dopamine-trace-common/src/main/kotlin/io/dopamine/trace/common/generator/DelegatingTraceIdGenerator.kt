package io.dopamine.trace.common.generator

import io.dopamine.id.generator.core.IdGenerator

/**
 * [TraceIdGenerator] implementation that delegates to a general-purpose [IdGenerator].
 *
 * This allows reuse of existing ID generation strategies (UUID, Snowflake, Hashed, etc)
 * for generating trace IDs in a consistent and configurable way.
 *
 * Example usage:
 * ```
 * val traceIdGenerator = DelegatingTraceIdGenerator(UuidGenerator())
 * val traceId = traceIdGenerator.generate()
 * ```
 *
 * This approach enables integration with the ID generator module,
 * allowing unified configuration and testing.
 */
class DelegatingTraceIdGenerator(
    private val idGenerator: IdGenerator,
) : TraceIdGenerator {
    /**
     * Generates a trace ID by delegating to the underlying [IdGenerator].
     */
    override fun generate(): String = idGenerator.generate()
}
