package io.dopamine.i18n.resolver

import org.springframework.context.MessageSource
import java.util.Locale

/**
 * [MessageResolver] implementation backed by Spring's [org.springframework.context.MessageSource].
 *
 * If a message cannot be resolved, the key itself is returned as a fallback.
 */
class SpringMessageResolver(
    private val messageSource: MessageSource,
) : MessageResolver {
    override fun resolve(
        key: String,
        locale: Locale,
    ): String = messageSource.getMessage(key, null, null, locale) ?: key
}
