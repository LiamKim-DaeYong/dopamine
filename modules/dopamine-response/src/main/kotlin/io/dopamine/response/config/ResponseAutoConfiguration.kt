package io.dopamine.response.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.dopamine.response.advice.ApiResponseAdvice
import io.dopamine.response.code.ResponseCodeRegistry
import io.dopamine.response.exception.GlobalExceptionHandler
import io.dopamine.response.trace.DefaultTraceIdResolver
import io.dopamine.response.trace.TraceIdConfig
import io.dopamine.response.trace.TraceIdResolver
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(
    name = [ResponsePropertyKeys.ENABLED],
    havingValue = "true",
    matchIfMissing = true,
)
@Import(
    ApiResponseAdvice::class,
    GlobalExceptionHandler::class,
    TraceIdConfig::class,
    ResponseCodeRegistry::class,
)
@EnableConfigurationProperties(ResponseProperties::class)
class ResponseAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

    @Bean
    fun traceIdResolver(props: ResponseProperties): TraceIdResolver {
        return DefaultTraceIdResolver(props)
    }
}
