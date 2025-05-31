package io.dopamine.trace.mvc.request

import io.dopamine.trace.core.request.RequestTraceContext
import jakarta.servlet.http.HttpServletRequest

/**
 * [RequestTraceContext] implementation that extracts headers and attributes from a [HttpServletRequest].
 *
 * Used in servlet-based environments such as Spring MVC.
 */
class ServletTraceContext(
    private val request: HttpServletRequest,
) : RequestTraceContext {
    override fun getHeader(name: String): String? = request.getHeader(name)

    override fun getAttribute(name: String): String? = request.getAttribute(name)?.toString()
}
