package io.dopamine.starter.common.autoconfig.generator

import io.dopamine.id.generator.IdGeneratorFactory
import io.dopamine.id.generator.config.IdGeneratorProperties
import io.dopamine.id.generator.config.IdGeneratorPropertyKeys
import io.dopamine.id.generator.core.IdGenerator
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnProperty(
    prefix = IdGeneratorPropertyKeys.PREFIX,
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true,
)
@EnableConfigurationProperties(IdGeneratorProperties::class)
class IdGeneratorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun idGenerator(properties: IdGeneratorProperties): IdGenerator = IdGeneratorFactory.create(properties)
}
