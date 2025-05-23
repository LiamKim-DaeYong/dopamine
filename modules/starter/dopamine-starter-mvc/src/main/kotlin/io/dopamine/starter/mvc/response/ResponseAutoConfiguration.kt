package io.dopamine.starter.mvc.response

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.config.ResponsePropertyKeys
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.response.core.trace.TraceIdResolver
import io.dopamine.response.mvc.advice.DopamineResponseAdvice
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(DopamineResponseAdvice::class)
@ConditionalOnProperty(
    name = [ResponsePropertyKeys.ENABLED],
    havingValue = "true",
    matchIfMissing = true,
)
@EnableConfigurationProperties(ResponseProperties::class)
class ResponseAutoConfiguration {
    @Bean
    fun dopamineResponseFactory(
        props: ResponseProperties,
        traceIdResolver: TraceIdResolver,
    ): DopamineResponseFactory = DopamineResponseFactory(props, traceIdResolver)

    @Bean
    fun dopamineResponseAdvice(
        factory: DopamineResponseFactory,
        objectMapper: ObjectMapper,
        request: HttpServletRequest,
    ): DopamineResponseAdvice = DopamineResponseAdvice(factory, objectMapper, request)
}
