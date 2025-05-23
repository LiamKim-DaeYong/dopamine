package io.dopamine.response.mvc.trace

import io.dopamine.response.core.trace.TraceContext
import jakarta.servlet.http.HttpServletRequest

class ServletTraceContext(
    private val request: HttpServletRequest,
) : TraceContext {
    override fun getHeader(name: String): String? = request.getHeader(name)

    override fun getAttribute(name: String): String? = request.getAttribute(name)?.toString()
}
