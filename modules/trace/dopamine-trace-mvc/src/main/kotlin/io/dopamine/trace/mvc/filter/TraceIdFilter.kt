package io.dopamine.trace.mvc.filter

import io.dopamine.trace.core.generator.TraceIdGenerator
import io.dopamine.trace.core.store.TraceIdStore
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Servlet filter that generates and stores a traceId at the start of each request.
 *
 * The traceId is cleared after request completion. Designed for use with [TraceIdStore] and [TraceIdGenerator].
 */
class TraceIdFilter(
    private val generator: TraceIdGenerator,
    private val store: TraceIdStore,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val traceId = generator.generate()
        store.setCurrentTraceId(traceId)

        try {
            filterChain.doFilter(request, response)
        } finally {
            store.clear()
        }
    }
}
