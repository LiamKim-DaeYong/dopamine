package io.dopamine.response.trace

import io.dopamine.response.config.ResponseProperties
import jakarta.servlet.http.HttpServletRequest
import java.util.UUID

class DefaultTraceIdResolver(
    private val props: ResponseProperties,
) : TraceIdResolver {
    override fun resolve(request: HttpServletRequest): String {
        val headerName = props.metaOptions.traceIdHeader
        return request.getHeader(headerName) ?: UUID.randomUUID().toString()
    }
}
