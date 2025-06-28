package io.dopamine.trace.mvc.request

import io.dopamine.core.resolver.RequestTraceContext
import jakarta.servlet.http.HttpServletRequest

/**
 * [RequestTraceContext] implementation that extracts headers and attributes from an [HttpServletRequest].
 *
 * Used in servlet-based environments such as Spring MVC.
 */
class ServletTraceContext(
    private val request: HttpServletRequest,
) : RequestTraceContext {
    override fun getHeader(name: String): String? = name.takeIf { it.isNotBlank() }?.let { request.getHeader(it) }

    override fun getAttribute(name: String): String? =
        name.takeIf { it.isNotBlank() }?.let { request.getAttribute(it)?.toString() }
}
