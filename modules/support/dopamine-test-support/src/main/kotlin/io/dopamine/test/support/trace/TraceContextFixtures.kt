package io.dopamine.test.support.trace

import io.dopamine.response.core.trace.TraceContext

object TraceContextFixtures {
    fun dummy() =
        object : TraceContext {
            override fun getHeader(name: String): String? = null

            override fun getAttribute(name: String): String? = null
        }
}
