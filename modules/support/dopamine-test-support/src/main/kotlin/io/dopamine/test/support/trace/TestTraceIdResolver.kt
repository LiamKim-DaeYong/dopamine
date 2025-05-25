package io.dopamine.test.support.trace

import io.dopamine.response.core.trace.TraceContext
import io.dopamine.response.core.trace.TraceIdResolver

/**
 * A test double for TraceIdResolver that returns a fixed trace ID.
 */
class TestTraceIdResolver(
    private val fixedValue: String = "test-trace-id",
) : TraceIdResolver {
    override fun resolve(context: TraceContext): String = fixedValue
}
