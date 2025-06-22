package io.dopamine.i18n.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for Dopamine i18n behavior.
 * Bound to the "dopamine.i18n" prefix in application settings.
 */
@ConfigurationProperties(I18nPropertyKeys.PREFIX)
data class I18nProperties(
    /**
     * List of base paths for message bundles (e.g. classpath:/messages, classpath:/dopamine/messages)
     */
    var baseNames: List<String> = listOf("classpath:/messages"),
    /**
     * Default locale to use if none is specified (e.g. "en", "ko").
     */
    var defaultLocale: String = "en",
    /**
     * Encoding used to read message files (default: UTF-8).
     */
    var encoding: String = "UTF-8",
    /**
     * Whether to fall back to the JVM system locale.
     */
    var fallbackToSystemLocale: Boolean = true,
)
