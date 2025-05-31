package io.dopamine.starter.mvc.response

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.config.ResponsePropertyKeys
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.response.mvc.advice.DopamineResponseAdvice
import io.dopamine.trace.core.resolver.TraceIdResolver
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnClass(DopamineResponseAdvice::class)
@ConditionalOnProperty(
    name = [ResponsePropertyKeys.ENABLED],
    havingValue = "true",
    matchIfMissing = true,
)
@EnableConfigurationProperties(ResponseProperties::class)
class ResponseAutoConfiguration {
    @Bean
    fun dopamineResponseFactory(props: ResponseProperties): DopamineResponseFactory = DopamineResponseFactory(props)

    @Bean
    fun dopamineResponseAdvice(
        factory: DopamineResponseFactory,
        objectMapper: ObjectMapper,
        request: HttpServletRequest,
        traceIdResolver: TraceIdResolver,
    ): DopamineResponseAdvice = DopamineResponseAdvice(factory, traceIdResolver, objectMapper, request)
}
