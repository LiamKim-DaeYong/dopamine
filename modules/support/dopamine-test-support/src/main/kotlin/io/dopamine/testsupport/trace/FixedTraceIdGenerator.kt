package io.dopamine.testsupport.trace

import io.dopamine.logging.trace.TraceIdGenerator

class FixedTraceIdGenerator(
    private val fixed: String,
) : TraceIdGenerator {
    override fun generate(): String = fixed
}
