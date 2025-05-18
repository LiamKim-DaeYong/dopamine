package io.dopamine.response.trace

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

class TraceIdConfig : WebMvcConfigurer {
    @Bean
    @ConditionalOnMissingBean
    fun traceIdResolver(): TraceIdResolver = DefaultTraceIdResolver()

    @Bean
    @ConditionalOnProperty(
        name = ["dopamine.response.formatting.meta-options.include-trace-id"],
        havingValue = "true",
        matchIfMissing = true,
    )
    fun traceIdInterceptor(traceIdResolver: TraceIdResolver): TraceIdInterceptor = TraceIdInterceptor(traceIdResolver)

    override fun addInterceptors(registry: InterceptorRegistry) {
        val interceptor = runCatching { traceIdInterceptor(traceIdResolver()) }.getOrNull()
        if (interceptor != null) {
            registry.addInterceptor(interceptor)
        }
    }
}
