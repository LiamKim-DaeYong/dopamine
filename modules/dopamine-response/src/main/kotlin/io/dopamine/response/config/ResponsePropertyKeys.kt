package io.dopamine.response.config

object ResponsePropertyKeys {
    const val PREFIX = "dopamine.response"
    const val ENABLED = "$PREFIX.enabled"
    const val INCLUDE_META = "$PREFIX.include-meta"
    const val INCLUDE_TRACE_ID = "$PREFIX.meta-options.include-trace-id"
    const val TRACE_ID_KEY = "$PREFIX.meta-options.trace-id-key"
    const val TRACE_ID_HEADER = "$PREFIX.meta-options.trace-id-header"
    const val INCLUDE_PAGING = "$PREFIX.meta-options.include-paging"
}
