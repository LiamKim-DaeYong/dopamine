package io.dopamine.response.trace

import jakarta.servlet.http.HttpServletRequest

fun interface TraceIdResolver {
    fun resolve(request: HttpServletRequest): String
}
