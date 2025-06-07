package io.dopamine.test.support.trace

import io.dopamine.trace.common.request.RequestTraceContext

object TraceContextFixtures {
    fun dummy() =
        object : RequestTraceContext {
            override fun getHeader(name: String): String? = null

            override fun getAttribute(name: String): String? = null
        }
}
