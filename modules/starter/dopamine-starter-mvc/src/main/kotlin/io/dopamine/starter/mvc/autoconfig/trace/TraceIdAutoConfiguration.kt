package io.dopamine.starter.mvc.autoconfig.trace

import io.dopamine.trace.common.generator.TraceIdGenerator
import io.dopamine.trace.common.generator.UuidTraceIdGenerator
import io.dopamine.trace.common.resolver.CompositeTraceIdResolver
import io.dopamine.trace.common.resolver.HeaderTraceIdResolver
import io.dopamine.trace.common.resolver.TraceIdResolver
import io.dopamine.trace.common.store.TraceIdStore
import io.dopamine.trace.mvc.config.TraceProperties
import io.dopamine.trace.mvc.filter.TraceIdFilter
import io.dopamine.trace.mvc.resolver.MdcTraceIdResolver
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
 * - [io.dopamine.trace.common.generator.TraceIdGenerator]: generates a unique traceId for each request
 * - [io.dopamine.trace.common.store.TraceIdStore]: stores the traceId in MDC
 * - [io.dopamine.trace.mvc.filter.TraceIdFilter]: servlet filter that manages the traceId lifecycle
 *
 * Configuration is bound to the `dopamine.trace` prefix via [io.dopamine.trace.mvc.config.TraceProperties].
 */
@AutoConfiguration
@EnableConfigurationProperties(TraceProperties::class)
class TraceIdAutoConfiguration {
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

    @AutoConfiguration
    class TraceIdResolverConfig {
        @Bean
        @ConditionalOnMissingBean(TraceIdResolver::class)
        fun traceIdResolver(props: TraceProperties): TraceIdResolver {
            val header = HeaderTraceIdResolver(props.traceIdHeader)
            val mdc = MdcTraceIdResolver(props.traceIdKey)
            return CompositeTraceIdResolver(listOf(header, mdc))
        }
    }
}
