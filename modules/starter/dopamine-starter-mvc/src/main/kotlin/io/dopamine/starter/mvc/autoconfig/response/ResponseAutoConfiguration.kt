package io.dopamine.starter.mvc.autoconfig.response

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.core.resolver.MessageResolver
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.config.ResponsePropertyKeys
import io.dopamine.response.common.factory.DopamineResponseFactory
import io.dopamine.response.common.metadata.CommonErrorMetadataProvider
import io.dopamine.response.common.metadata.CommonSuccessMetadataProvider
import io.dopamine.response.common.metadata.DefaultResponseMetadataResolver
import io.dopamine.response.common.metadata.MetaContributor
import io.dopamine.response.common.metadata.ResponseMetadataProvider
import io.dopamine.response.common.metadata.ResponseMetadataResolver
import io.dopamine.response.mvc.advice.DopamineErrorResponseAdvice
import io.dopamine.response.mvc.advice.DopamineResponseAdvice
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * AutoConfiguration for common response handling in Spring MVC.
 *
 * Registers core components such as [DopamineResponseFactory], response advices,
 * metadata registry, and integrates with trace & i18n modules.
 */
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
    fun commonSuccessMetadataProvider(): ResponseMetadataProvider = CommonSuccessMetadataProvider()

    @Bean
    fun commonErrorMetadataProvider(): ResponseMetadataProvider = CommonErrorMetadataProvider()

    @Bean
    fun responseMetadataResolver(providers: List<ResponseMetadataProvider>): ResponseMetadataResolver =
        DefaultResponseMetadataResolver(providers)

    @Bean
    @ConditionalOnMissingBean
    fun dopamineResponseFactory(
        props: ResponseProperties,
        resolver: ResponseMetadataResolver,
        messageResolver: MessageResolver,
        contributors: ObjectProvider<List<MetaContributor>>,
    ): DopamineResponseFactory =
        DopamineResponseFactory(
            props = props,
            resolver = resolver,
            messageResolver = messageResolver,
            contributors = contributors.ifAvailable.orEmpty(),
        )

    @Bean
    @ConditionalOnMissingBean
    fun dopamineResponseAdvice(
        factory: DopamineResponseFactory,
        objectMapper: ObjectMapper,
        props: ResponseProperties,
    ): DopamineResponseAdvice = DopamineResponseAdvice(factory, objectMapper, props)

    @Bean
    @ConditionalOnMissingBean
    fun dopamineErrorResponseAdvice(
        factory: DopamineResponseFactory,
        resolver: ResponseMetadataResolver,
    ): DopamineErrorResponseAdvice = DopamineErrorResponseAdvice(factory, resolver)
}
