package io.dopamine.starter.common

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
    prefix = DocsPropertyKeys.PREFIX,
    name = [DocsPropertyKeys.ENABLED],
    havingValue = "true",
    matchIfMissing = true,
)
@EnableConfigurationProperties(DocsProperties::class)
class DocsAutoConfiguration {
    @Bean
    @ConditionalOnProperty(
        prefix = DocsPropertyKeys.Swagger.PREFIX,
        name = [DocsPropertyKeys.ENABLED],
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

    /**
     * Registers the default Swagger group for Dopamine.
     * This group is always enabled and fixed to scan the core package.
     */
    @Bean
    fun defaultGroupedOpenApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group(DocsPropertyKeys.Swagger.DEFAULT_GROUP_NAME)
            .packagesToScan(DocsPropertyKeys.Swagger.DEFAULT_BASE_PACKAGE)
            .build()

    /**
     * Registers additional user-defined Swagger groups.
     * These are optional and configured via properties.
     */
    @Bean
    fun additionalGroupedOpenApis(props: DocsProperties): List<GroupedOpenApi> =
        props.swagger.additionalGroups.map { group ->
            GroupedOpenApi
                .builder()
                .group(group.name)
                .packagesToScan(*group.basePackages.toTypedArray())
                .build()
        }
}
