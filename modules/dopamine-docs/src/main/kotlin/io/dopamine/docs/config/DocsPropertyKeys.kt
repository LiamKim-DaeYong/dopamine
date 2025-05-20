package io.dopamine.docs.config

object DocsPropertyKeys {
    const val PREFIX = "dopamine.docs"
    const val ENABLED = "$PREFIX.enabled"

    object Swagger {
        const val PREFIX = "${DocsPropertyKeys.PREFIX}.swagger"
        const val ENABLED = "$PREFIX.enabled"
        const val GROUP = "$PREFIX.group"
        const val TITLE = "$PREFIX.title"
        const val DESCRIPTION = "$PREFIX.description"
        const val VERSION = "$PREFIX.version"
        const val BASE_PATH = "$PREFIX.base-path"
    }
}
