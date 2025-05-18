package io.dopamine.response.trace

import io.dopamine.response.config.ResponseProperties
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.servlet.HandlerInterceptor

class TraceIdInterceptor(
    private val traceIdResolver: TraceIdResolver,
    private val props: ResponseProperties,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val traceId = traceIdResolver.resolve(request)
        val mdcKey = props.metaOptions.traceIdKey
        MDC.put(mdcKey, traceId)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val mdcKey = props.metaOptions.traceIdKey
        MDC.remove(mdcKey)
    }
}
