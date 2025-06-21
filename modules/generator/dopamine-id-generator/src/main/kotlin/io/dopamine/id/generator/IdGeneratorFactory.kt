package io.dopamine.id.generator

import IdGenerator

/**
 * Factory for creating [IdGenerator] instances based on [IdType].
 */
object IdGeneratorFactory {
    fun from(type: IdType, nodeId: Long = 1): IdGenerator = when (type) {
        IdType.UUID -> UuidGenerator()
        IdType.SNOWFLAKE -> SnowflakeIdGenerator(nodeId)
    }
}
