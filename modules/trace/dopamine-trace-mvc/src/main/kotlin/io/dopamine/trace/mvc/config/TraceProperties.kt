package io.dopamine.trace.mvc.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for Dopamine trace behavior.
 * Bound to the "dopamine.trace" prefix in application settings.
 */
@ConfigurationProperties(TracePropertyKeys.PREFIX)
data class TraceProperties(
    /**
     * The key name to use for traceId in the response (e.g., "traceId", "X-Trace-ID").
     */
    val traceIdKey: String = "traceId",
    /**
     * The request header name from which to extract the traceId.
     */
    val traceIdHeader: String = "X-Trace-ID",
)
