package io.dopamine.i18n.resolver

import java.util.Locale

/**
 * Resolves localized messages using a message key and locale.
 *
 * Typically used to retrieve i18n messages from resource bundles.
 */
fun interface MessageResolver {
    fun resolve(
        key: String,
        locale: Locale,
    ): String
}
