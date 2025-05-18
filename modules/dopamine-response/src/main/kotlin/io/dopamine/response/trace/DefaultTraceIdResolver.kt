package io.dopamine.response.trace

import jakarta.servlet.http.HttpServletRequest
import java.util.UUID

class DefaultTraceIdResolver : TraceIdResolver {
    override fun resolve(request: HttpServletRequest): String {
        return request.getHeader(TraceIdConstants.HEADER_NAME)
            ?: UUID.randomUUID().toString()
    }
}
