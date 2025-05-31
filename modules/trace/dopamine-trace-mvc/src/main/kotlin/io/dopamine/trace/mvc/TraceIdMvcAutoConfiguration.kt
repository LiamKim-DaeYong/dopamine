package io.dopamine.trace.mvc

import io.dopamine.trace.core.generator.TraceIdGenerator
import io.dopamine.trace.core.generator.UuidTraceIdGenerator
import io.dopamine.trace.core.store.TraceIdStore
import io.dopamine.trace.mvc.config.TraceProperties
import io.dopamine.trace.mvc.filter.TraceIdFilter
import io.dopamine.trace.mvc.store.MdcTraceIdStore
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Autoconfiguration for traceId generation and storage in servlet-based environments.
 *
 * Registers:
 * - [TraceIdGenerator]: generates a unique traceId for each request
 * - [TraceIdStore]: stores the traceId in MDC
 * - [TraceIdFilter]: servlet filter that manages the traceId lifecycle
 *
 * Configuration is bound to the `dopamine.trace` prefix via [TraceProperties].
 */
@AutoConfiguration
@EnableConfigurationProperties(TraceProperties::class)
class TraceIdMvcAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun traceIdGenerator(): TraceIdGenerator = UuidTraceIdGenerator()

    @Bean
    @ConditionalOnMissingBean
    fun traceIdStore(props: TraceProperties): TraceIdStore = MdcTraceIdStore(props.traceIdKey)

    @Bean
    @ConditionalOnMissingBean
    fun traceIdFilter(
        generator: TraceIdGenerator,
        store: TraceIdStore,
    ): FilterRegistrationBean<OncePerRequestFilter> {
        val filter = TraceIdFilter(generator, store)
        return FilterRegistrationBean<OncePerRequestFilter>(filter).apply {
            order = Ordered.HIGHEST_PRECEDENCE
        }
    }
}
