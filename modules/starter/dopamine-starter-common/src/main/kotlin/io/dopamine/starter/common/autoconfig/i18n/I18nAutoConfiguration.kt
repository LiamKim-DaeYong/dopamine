package io.dopamine.starter.common.autoconfig.i18n

import io.dopamine.core.resolver.MessageResolver
import io.dopamine.i18n.config.I18nProperties
import io.dopamine.i18n.resolver.SpringMessageResolver
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.Locale

@AutoConfiguration
@EnableConfigurationProperties(I18nProperties::class)
class I18nAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(MessageSource::class)
    fun messageSource(props: I18nProperties): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            val baseNames = (props.baseNames + "classpath:/dopamine/messages").distinct()
            setBasenames(*baseNames.toTypedArray())
            setDefaultEncoding(props.encoding)
            setFallbackToSystemLocale(props.fallbackToSystemLocale) // defaults to false in Spring Boot 3
            setDefaultLocale(Locale.forLanguageTag(props.defaultLocale))
        }

    @Bean
    @ConditionalOnMissingBean(MessageResolver::class)
    fun messageResolver(messageSource: MessageSource): MessageResolver =
        SpringMessageResolver(
            messageSource = messageSource,
            localeProvider = { LocaleContextHolder.getLocale() },
        )
}
