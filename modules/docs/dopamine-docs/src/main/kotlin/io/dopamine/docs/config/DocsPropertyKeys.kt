package io.dopamine.docs.config

object DocsPropertyKeys {
    const val PREFIX = "dopamine.docs"

    const val ENABLED = "$PREFIX.enabled"

    object Swagger {
        const val SWAGGER_PREFIX = "$PREFIX.swagger."

        const val ENABLED = "$SWAGGER_PREFIX.enabled"
        const val TITLE = "$SWAGGER_PREFIX.title"
        const val VERSION = "$SWAGGER_PREFIX.version"
        const val DESCRIPTION = "$SWAGGER_PREFIX.description"
        const val BASE_PACKAGE = "$SWAGGER_PREFIX.base-package"
    }

    object Static {
        const val STATIC_PREFIX = "$PREFIX.static"

        const val ENABLED = "$STATIC_PREFIX.enabled"
        const val PATH = "$STATIC_PREFIX.path"
    }

    object Admin {
        const val ADMIN_PREFIX = "$PREFIX.admin"

        const val ENABLED = "$ADMIN_PREFIX.enabled"
    }
}
