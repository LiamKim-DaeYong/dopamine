package io.dopamine.response.trace

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.servlet.HandlerInterceptor

class TraceIdInterceptor(
    private val traceIdResolver: TraceIdResolver,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val traceId = traceIdResolver.resolve(request)
        MDC.put(TraceIdConstants.MDC_KEY, traceId)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        MDC.remove(TraceIdConstants.MDC_KEY)
    }
}
