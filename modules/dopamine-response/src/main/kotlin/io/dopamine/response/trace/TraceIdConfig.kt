package io.dopamine.response.trace

import io.dopamine.response.config.ResponsePropertyKeys
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class TraceIdConfig(
    private val traceIdResolver: TraceIdResolver,
) : WebMvcConfigurer {
    @Bean
    @ConditionalOnProperty(
        name = [ResponsePropertyKeys.INCLUDE_TRACE_ID],
        havingValue = "true",
        matchIfMissing = true,
    )
    fun traceIdInterceptor(): TraceIdInterceptor = TraceIdInterceptor(traceIdResolver)

    override fun addInterceptors(registry: InterceptorRegistry) {
        runCatching { traceIdInterceptor() }
            .getOrNull()
            ?.let { registry.addInterceptor(it) }
    }
}
