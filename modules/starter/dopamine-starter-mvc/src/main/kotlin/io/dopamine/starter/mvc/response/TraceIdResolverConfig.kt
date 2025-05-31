package io.dopamine.starter.mvc.response

import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.trace.core.resolver.CompositeTraceIdResolver
import io.dopamine.trace.core.resolver.HeaderTraceIdResolver
import io.dopamine.trace.core.resolver.TraceIdResolver
import io.dopamine.trace.mvc.resolver.MdcTraceIdResolver
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

@AutoConfiguration
class TraceIdResolverConfig {
    @Bean
    fun headerTraceIdResolver(props: ResponseProperties): HeaderTraceIdResolver =
        HeaderTraceIdResolver(props.metaOptions.traceIdHeader)

    @Bean
    fun mdcTraceIdResolver(props: ResponseProperties): MdcTraceIdResolver =
        MdcTraceIdResolver(props.metaOptions.traceIdKey)

    @Bean
    @ConditionalOnMissingBean
    fun traceIdResolver(
        header: HeaderTraceIdResolver,
        mdc: MdcTraceIdResolver,
    ): TraceIdResolver = CompositeTraceIdResolver(listOf(header, mdc))
}
