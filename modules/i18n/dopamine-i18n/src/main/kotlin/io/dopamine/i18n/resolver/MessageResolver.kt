package io.dopamine.i18n.resolver

/**
 * Resolves localized messages using a message key and locale.
 *
 * Typically used to retrieve i18n messages from resource bundles.
 */
fun interface MessageResolver {
    fun resolve(
        messageKey: String?,
        defaultMessage: String?,
    ): String?
}
