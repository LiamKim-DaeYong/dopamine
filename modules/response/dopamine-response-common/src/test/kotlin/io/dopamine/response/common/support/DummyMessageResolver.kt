package io.dopamine.response.common.support

import io.dopamine.core.resolver.MessageResolver

/**
 * A MessageResolver that always returns the defaultMessage if present,
 * or the messageKey itself as a fallback.
 *
 * Intended for use in non-i18n-focused unit tests.
 */
class DummyMessageResolver : MessageResolver {
    override fun resolve(
        messageKey: String?,
        defaultMessage: String?,
    ): String? = defaultMessage ?: messageKey
}
