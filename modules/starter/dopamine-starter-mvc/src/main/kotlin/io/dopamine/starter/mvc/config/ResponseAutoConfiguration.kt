package io.dopamine.starter.mvc.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.i18n.resolver.MessageResolver
import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.config.ResponsePropertyKeys
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.response.mvc.advice.DopamineErrorResponseAdvice
import io.dopamine.response.mvc.advice.DopamineResponseAdvice
import io.dopamine.response.mvc.meta.ResponseMetaBuilder
import io.dopamine.trace.core.resolver.TraceIdResolver
import io.dopamine.trace.mvc.config.TraceProperties
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
    fun dopamineResponseFactory(
        props: ResponseProperties,
        messageResolver: MessageResolver,
    ): DopamineResponseFactory = DopamineResponseFactory(props, messageResolver)

    @Bean
    fun responseMetaBuilder(
        traceIdResolver: TraceIdResolver,
        traceProperties: TraceProperties,
        request: HttpServletRequest,
    ): ResponseMetaBuilder = ResponseMetaBuilder(traceIdResolver, traceProperties, request)

    @Bean
    fun dopamineResponseAdvice(
        factory: DopamineResponseFactory,
        objectMapper: ObjectMapper,
        metaBuilder: ResponseMetaBuilder,
    ): DopamineResponseAdvice = DopamineResponseAdvice(factory, objectMapper, metaBuilder)

    @Bean
    fun dopamineErrorResponseAdvice(
        factory: DopamineResponseFactory,
        metaBuilder: ResponseMetaBuilder,
    ): DopamineErrorResponseAdvice = DopamineErrorResponseAdvice(factory, metaBuilder)
}
