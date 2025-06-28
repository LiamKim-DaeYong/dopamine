package io.dopamine.i18n.resolver

import io.dopamine.core.resolver.MessageResolver
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Locale

/**
 * [MessageResolver] implementation backed by Spring's [org.springframework.context.MessageSource].
 *
 * If a message cannot be resolved, the key itself is returned as a fallback.
 */
class SpringMessageResolver(
    private val messageSource: MessageSource,
    private val localeProvider: () -> Locale = { LocaleContextHolder.getLocale() },
) : MessageResolver {
    override fun resolve(
        messageKey: String?,
        defaultMessage: String?,
    ): String? {
        val locale = localeProvider()

        return messageKey?.let {
            try {
                messageSource.getMessage(it, null, null, locale)
            } catch (ex: NoSuchMessageException) {
                defaultMessage
            }
        } ?: defaultMessage
    }
}
