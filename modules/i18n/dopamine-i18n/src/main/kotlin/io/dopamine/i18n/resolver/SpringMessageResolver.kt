package io.dopamine.i18n.resolver

import io.dopamine.core.resolver.MessageResolver
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import java.util.Locale

/**
 * [MessageResolver] implementation backed by Spring's [MessageSource].
 *
 * If a message cannot be resolved, the default message is returned.
 */
class SpringMessageResolver(
    private val messageSource: MessageSource,
    private val localeProvider: () -> Locale = { LocaleContextHolder.getLocale() },
) : MessageResolver {
    override fun resolve(
        messageKey: String?,
        defaultMessage: String?,
        args: Array<out Any>?,
    ): String? {
        if (messageKey.isNullOrBlank()) return defaultMessage

        return try {
            messageSource.getMessage(messageKey, args, defaultMessage, localeProvider())
        } catch (ex: NoSuchMessageException) {
            defaultMessage
        }
    }
}
