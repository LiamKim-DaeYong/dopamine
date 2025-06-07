package io.dopamine.trace.mvc.store

import io.dopamine.trace.common.store.TraceIdStore
import org.slf4j.MDC

/**
 * [TraceIdStore] implementation that stores traceId in SLF4J MDC.
 *
 * Typically used in servlet-based environments to make traceId available in logging context.
 */
class MdcTraceIdStore(
    private val key: String,
) : TraceIdStore {
    override fun getCurrentTraceId(): String? = MDC.get(key)

    override fun setCurrentTraceId(traceId: String) {
        MDC.put(key, traceId)
    }

    override fun clear() {
        MDC.remove(key)
    }
}
