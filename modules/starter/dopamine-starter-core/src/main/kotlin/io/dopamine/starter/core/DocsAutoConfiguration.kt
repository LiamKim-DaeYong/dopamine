package io.dopamine.starter.core

import io.dopamine.docs.config.DocsProperties
import io.dopamine.docs.config.DocsPropertyKeys
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnProperty(
    name = [DocsPropertyKeys.ENABLED],
    havingValue = "true",
    matchIfMissing = true,
)
@EnableConfigurationProperties(DocsProperties::class)
class DocsAutoConfiguration {
    @Bean
    @ConditionalOnProperty(
        prefix = DocsPropertyKeys.Swagger.ENABLED,
        name = ["enabled"],
        havingValue = "true",
        matchIfMissing = true,
    )
    fun openAPI(props: DocsProperties): OpenAPI =
        OpenAPI().info(
            Info()
                .title(props.swagger.title)
                .version(props.swagger.version)
                .description(props.swagger.description),
        )

    @Bean
    @ConditionalOnProperty(
        prefix = "dopamine.docs.swagger",
        name = ["enabled"],
        havingValue = "true",
        matchIfMissing = true,
    )
    fun groupedOpenApi(props: DocsProperties): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("default")
            .packagesToScan(props.swagger.basePackage)
            .build()
}
