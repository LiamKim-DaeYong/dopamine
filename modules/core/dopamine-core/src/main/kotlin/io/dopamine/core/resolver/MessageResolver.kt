package io.dopamine.core.resolver

/**
 * Resolves a message key into a localized message string.
 *
 * This interface is designed to be transport-agnostic and applicable across
 * various modules (e.g., response, auth, batch, notification, docs).
 */
interface MessageResolver {
    /**
     * Resolves the given message key into a localized message.
     *
     * @param messageKey the message key to resolve (e.g., "auth.token.expired")
     * @param defaultMessage fallback message to use if no resolution is found
     * @param args optional arguments to bind into the message format
     * @return the resolved message, or the default if not found
     */
    fun resolve(
        messageKey: String?,
        defaultMessage: String?,
        args: Array<out Any>? = null,
    ): String?
}
