package io.dopamine.trace.core

/**
 * Stores and retrieves the current traceId during a request lifecycle.
 *
 * Implementations may use MDC, ThreadLocal, or other context mechanisms
 * depending on the runtime environment.
 */
interface TraceIdContext {
    fun getCurrentTraceId(): String?
    fun setCurrentTraceId(traceId: String)
    fun clear()
}
