package io.dopamine.trace.core.store

/**
 * Abstraction for storing and retrieving the current traceId during request processing.
 *
 * Typical implementations may use MDC, ThreadLocal, or other context-specific mechanisms.
 */
interface TraceIdStore {
    fun getCurrentTraceId(): String?

    fun setCurrentTraceId(traceId: String)

    fun clear()
}
