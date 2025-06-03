package io.dopamine.i18n.config

import io.dopamine.i18n.resolver.MessageResolver
import io.dopamine.i18n.resolver.SpringMessageResolver
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.Locale

@Configuration
@EnableConfigurationProperties(I18nProperties::class)
class I18nAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MessageSource::class)
    fun messageSource(props: I18nProperties): MessageSource {
        return ReloadableResourceBundleMessageSource().apply {
            setBasename(props.basename)
            setDefaultEncoding(props.encoding)
            setFallbackToSystemLocale(props.fallbackToSystemLocale)
            setDefaultLocale(Locale.forLanguageTag(props.defaultLocale))
        }
    }

    @Bean
    @ConditionalOnMissingBean(MessageResolver::class)
    fun messageResolver(messageSource: MessageSource): MessageResolver {
        return SpringMessageResolver(messageSource)
    }
}
