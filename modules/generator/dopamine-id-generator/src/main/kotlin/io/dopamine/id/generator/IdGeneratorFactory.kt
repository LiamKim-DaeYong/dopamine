package io.dopamine.id.generator

import io.dopamine.id.generator.config.IdGeneratorProperties
import io.dopamine.id.generator.core.IdGenerator
import io.dopamine.id.generator.core.IdType
import io.dopamine.id.generator.strategy.HashedIdGenerator
import io.dopamine.id.generator.strategy.SnowflakeIdGenerator
import io.dopamine.id.generator.strategy.UuidGenerator
import io.dopamine.id.generator.util.SnowflakeEpochUtil

/**
 * Factory for creating [IdGenerator] instances based on [IdType].
 */
object IdGeneratorFactory {
    fun create(properties: IdGeneratorProperties): IdGenerator =
        when (properties.type) {
            IdType.UUID ->
                UuidGenerator(
                    upperCase = properties.uuid.upperCase,
                    withoutHyphen = properties.uuid.withoutHyphen,
                )

            IdType.SNOWFLAKE -> {
                SnowflakeIdGenerator(
                    nodeId = properties.snowflake.nodeId,
                    customEpoch = SnowflakeEpochUtil.parseOrDefault(properties.snowflake.customEpoch),
                )
            }

            IdType.HASHED ->
                HashedIdGenerator(
                    length = properties.hashed.length,
                    alphabet = properties.hashed.alphabet,
                )

            IdType.CUSTOM -> throw IllegalArgumentException(
                "CUSTOM strategy must be provided manually by user",
            )
        }
}
