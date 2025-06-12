package io.dopamine.response.common.config

import io.dopamine.core.format.TimestampFormat
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for Dopamine response behavior.
 * Bound to the "dopamine.response" prefix in application settings.
 */
@ConfigurationProperties(ResponsePropertyKeys.PREFIX)
data class ResponseProperties(
    /**
     * Whether to enable the Dopamine response module.
     */
    val enabled: Boolean = true,
    /**
     * Whether to include the "meta" field in responses (e.g., traceId, paging).
     */
    val includeMeta: Boolean = true,
    /**
     * Options for configuring the contents of the "meta" field.
     * Ignored if includeMeta is false.
     */
    val metaOptions: MetaOptions = MetaOptions(),
    /**
     * Timestamp format to be used in the response metadata.
     */
    val timestampFormat: TimestampFormat = TimestampFormat.ISO_8601,
    /**
     * Custom response codes and messages mapped by HTTP status.
     * Allows overriding default messages or internal codes.
     */
    val codes: List<CustomResponseCode> = emptyList(),
) {
    /**
     * Options for fine-grained control of metadata ("meta") fields.
     */
    data class MetaOptions(
        /**
         * Whether to include the traceId in the "meta" field.
         */
        val includeTraceId: Boolean = true,
        /**
         * Whether to include paging information in the "meta" field.
         * Automatically extracted if the response contains Page<T> or similar.
         */
        val includePaging: Boolean = true,
    )
}
