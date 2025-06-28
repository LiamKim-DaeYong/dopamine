package io.dopamine.starter.mvc.autoconfig.response

import io.dopamine.core.resolver.TraceIdResolver
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.factory.DopamineResponseFactory
import io.dopamine.response.common.metadata.MetaContributor
import io.dopamine.response.common.metadata.PagingMetaContributor
import io.dopamine.starter.mvc.support.TraceIdMetaContributor
import io.dopamine.trace.mvc.config.TraceProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

/**
 * AutoConfiguration that registers common MetaContributors for response metadata enrichment.
 *
 * Registered contributors:
 * - [TraceIdMetaContributor]: adds traceId to the meta-field in responses
 * - [PagingMetaContributor]: adds paging information if the response body is a Page or Slice
 *
 * These contributors are conditionally registered as Spring beans and injected into
 * [DopamineResponseFactory] to dynamic construct response metadata.
 *
 */
@AutoConfiguration
@ConditionalOnClass(TraceIdResolver::class)
class ResponseMetaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(TraceIdMetaContributor::class)
    fun traceIdMetaContributor(
        resolver: TraceIdResolver,
        props: TraceProperties,
    ): MetaContributor = TraceIdMetaContributor(resolver, props)

    @Bean
    @ConditionalOnMissingBean(PagingMetaContributor::class)
    fun pagingMetaContributor(props: ResponseProperties): MetaContributor =
        PagingMetaContributor(props.metaOptions.includePaging)
}
