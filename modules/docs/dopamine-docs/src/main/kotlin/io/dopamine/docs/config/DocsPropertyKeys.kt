package io.dopamine.docs.config

object DocsPropertyKeys {
    const val PREFIX = "dopamine.docs"
    const val ENABLED = "enabled"

    object Swagger {
        const val PREFIX = "${DocsPropertyKeys.PREFIX}.swagger"

        const val TITLE = "title"
        const val VERSION = "version"
        const val DESCRIPTION = "description"
        const val BASE_PACKAGE = "base-package"

        const val DEFAULT_GROUP_NAME = "dopamine"
        const val DEFAULT_BASE_PACKAGE = "io.dopamine"
    }

    object Static {
        const val PREFIX = "${DocsPropertyKeys.PREFIX}.static"
        const val PATH = "path"
    }

    object Admin {
        const val PREFIX = "${DocsPropertyKeys.PREFIX}.admin"
        const val ENABLED = "enabled"
        const val ENABLED_KEY = "$PREFIX.$ENABLED"
    }
}
