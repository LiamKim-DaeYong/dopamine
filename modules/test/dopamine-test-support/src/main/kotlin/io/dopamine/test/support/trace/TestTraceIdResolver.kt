package io.dopamine.test.support.trace

import io.dopamine.trace.common.request.RequestTraceContext
import io.dopamine.trace.common.resolver.TraceIdResolver

/**
 * A test double for TraceIdResolver that returns a fixed trace ID.
 */
class TestTraceIdResolver(
    private val fixedValue: String = "test-trace-id",
) : TraceIdResolver {
    override fun resolve(context: RequestTraceContext): String = fixedValue
}
