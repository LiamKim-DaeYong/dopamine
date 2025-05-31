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
    @ConditionalOnMissingBean(TraceIdResolver::class)
    fun traceIdResolver(props: ResponseProperties): TraceIdResolver {
        val header = HeaderTraceIdResolver(props.metaOptions.traceIdHeader)
        val mdc = MdcTraceIdResolver(props.metaOptions.traceIdKey)
        return CompositeTraceIdResolver(listOf(header, mdc))
    }
}
