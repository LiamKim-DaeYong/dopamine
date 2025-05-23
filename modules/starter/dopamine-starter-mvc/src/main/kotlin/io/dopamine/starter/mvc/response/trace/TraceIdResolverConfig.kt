package io.dopamine.starter.mvc.response.trace

import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.trace.CompositeTraceIdResolver
import io.dopamine.response.core.trace.HeaderTraceIdResolver
import io.dopamine.response.core.trace.TraceIdResolver
import io.dopamine.response.mvc.trace.MdcTraceIdResolver
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
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
