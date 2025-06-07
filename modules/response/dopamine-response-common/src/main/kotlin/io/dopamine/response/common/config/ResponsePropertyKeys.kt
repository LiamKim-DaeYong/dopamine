package io.dopamine.response.common.config

object ResponsePropertyKeys {
    const val PREFIX = "dopamine.response"

    const val ENABLED = "$PREFIX.enabled"
    const val INCLUDE_META = "$PREFIX.include-meta"

    object Meta {
        const val META_PREFIX = "$PREFIX.meta-options"

        const val INCLUDE_TRACE_ID = "$META_PREFIX.include-trace-id"
        const val INCLUDE_PAGING = "$META_PREFIX.include-paging"
    }
}
