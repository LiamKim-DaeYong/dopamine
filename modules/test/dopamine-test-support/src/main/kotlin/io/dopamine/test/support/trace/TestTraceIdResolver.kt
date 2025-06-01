package io.dopamine.test.support.trace

import io.dopamine.trace.core.request.RequestTraceContext
import io.dopamine.trace.core.resolver.TraceIdResolver

/**
 * A test double for TraceIdResolver that returns a fixed trace ID.
 */
class TestTraceIdResolver(
    private val fixedValue: String = "test-trace-id",
) : TraceIdResolver {
    override fun resolve(context: RequestTraceContext): String = fixedValue
}
