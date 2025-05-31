package io.dopamine.trace.core

/**
 * Generates a unique traceId for request tracking.
 *
 * Implementations can define custom formats such as UUID, timestamps, or prefixed values.
 */
fun interface TraceIdGenerator {
    fun generate(): String
}
