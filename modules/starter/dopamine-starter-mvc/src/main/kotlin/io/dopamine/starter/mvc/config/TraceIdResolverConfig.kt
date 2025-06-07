package io.dopamine.starter.mvc.config

import io.dopamine.trace.common.resolver.CompositeTraceIdResolver
import io.dopamine.trace.common.resolver.HeaderTraceIdResolver
import io.dopamine.trace.common.resolver.TraceIdResolver
import io.dopamine.trace.mvc.config.TraceProperties
import io.dopamine.trace.mvc.resolver.MdcTraceIdResolver
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

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
