package io.dopamine.response.common.support

import io.dopamine.core.resolver.MessageResolver

/**
 * A simple MessageResolver used for testing.
 * Resolves messages using a predefined map, or returns the defaultMessage.
 */
class DummyMessageResolver(
    private val messages: Map<String, String> = emptyMap(),
) : MessageResolver {
    override fun resolve(
        messageKey: String?,
        defaultMessage: String?,
        args: Array<out Any>?,
    ): String? {
        if (messageKey.isNullOrBlank()) return defaultMessage

        val raw = messages[messageKey] ?: defaultMessage ?: return null

        // Simple formatting support for {0}, {1}, ... using String.format-like logic
        return if (!args.isNullOrEmpty()) {
            try {
                raw.replace(Regex("""\{(\d+)}""")) { match ->
                    val index = match.groupValues[1].toIntOrNull() ?: return@replace match.value
                    args.getOrNull(index)?.toString() ?: match.value
                }
            } catch (e: Exception) {
                raw // Fallback to raw if formatting fails
            }
        } else {
            raw
        }
    }
}
