package io.dopamine.starter.mvc.autoconfig.response

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.core.resolver.MessageResolver
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.config.ResponsePropertyKeys
import io.dopamine.response.common.factory.DopamineResponseFactory
import io.dopamine.response.common.metadata.CommonErrorMetadata
import io.dopamine.response.common.metadata.CommonSuccessMetadata
import io.dopamine.response.common.metadata.DefaultResponseCodeRegistry
import io.dopamine.response.common.metadata.MetaContributor
import io.dopamine.response.common.metadata.ResponseCodeRegistry
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
    @ConditionalOnMissingBean
    fun responseCodeRegistry(props: ResponseProperties): ResponseCodeRegistry =
        DefaultResponseCodeRegistry(
            predefined = CommonSuccessMetadata.values + CommonErrorMetadata.values,
            custom = props.codes,
        )

    @Bean
    @ConditionalOnMissingBean
    fun dopamineResponseFactory(
        props: ResponseProperties,
        registry: ResponseCodeRegistry,
        messageResolver: MessageResolver,
        contributors: ObjectProvider<List<MetaContributor>>,
    ): DopamineResponseFactory =
        DopamineResponseFactory(
            props = props,
            registry = registry,
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
        registry: ResponseCodeRegistry,
    ): DopamineErrorResponseAdvice = DopamineErrorResponseAdvice(factory, registry)
}
