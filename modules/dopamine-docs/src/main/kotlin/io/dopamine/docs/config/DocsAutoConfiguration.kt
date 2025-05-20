package io.dopamine.docs.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(
    name = [DocsPropertyKeys.ENABLED],
    havingValue = "true",
    matchIfMissing = true,
)
@EnableConfigurationProperties(DocsProperties::class)
class DocsAutoConfiguration {
    @Bean
    @ConditionalOnProperty(
        name = [DocsPropertyKeys.Swagger.ENABLED],
        havingValue = "true",
        matchIfMissing = true,
    )
    fun publicApi(properties: DocsProperties): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group(properties.swagger.group)
            .pathsToMatch(properties.swagger.basePath)
            .build()
}
