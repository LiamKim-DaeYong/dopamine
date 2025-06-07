package io.dopamine.trace.common.generator

import java.util.UUID

/**
 * [TraceIdGenerator] implementation that generates traceIds using Java's UUID.
 */
class UuidTraceIdGenerator : TraceIdGenerator {
    override fun generate(): String = UUID.randomUUID().toString()
}
