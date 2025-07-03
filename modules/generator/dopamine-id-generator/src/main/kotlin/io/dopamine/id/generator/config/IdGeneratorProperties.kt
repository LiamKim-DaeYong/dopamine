package io.dopamine.id.generator.config

import io.dopamine.id.generator.core.IdType
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration holder for ID generator strategy and options.
 */
@ConfigurationProperties(IdGeneratorPropertyKeys.PREFIX)
data class IdGeneratorProperties(
    /**
     * The ID generation strategy to use.
     * Default is UUID.
     */
    val type: IdType = IdType.UUID,
    /**
     * Optional prefix to add at the beginning of generated IDs.
     * This affects the final output string but not uniqueness logic.
     */
    val prefix: String? = null,
    /**
     * Settings for UUID ID generation.
     */
    val uuid: UuidProperties = UuidProperties(),
    /**
     * Settings for HASHED ID generation.
     */
    val hashed: HashedProperties = HashedProperties(),
    /**
     * Settings for SNOWFLAKE ID generation.
     */
    val snowflake: SnowflakeProperties = SnowflakeProperties(),
) {
    /**
     * Configuration for UUID strategy.
     */
    data class UuidProperties(
        /**
         * Whether to convert UUID to uppercase.
         * Default is false (lowercase).
         */
        val upperCase: Boolean = false,
        /**
         * Whether to remove hyphens from UUID.
         * Default is false → keeps standard 8-4-4-4-12 format.
         */
        val withoutHyphen: Boolean = false,
    )

    /**
     * Configuration for HASHED ID strategy.
     */
    data class HashedProperties(
        /**
         * Desired length of the final hashed ID string.
         */
        val length: Int = 12,
        /**
         * Custom alphabet used for hash generation.
         * If null, a default character set will be used.
         */
        val alphabet: String? = null,
    )

    /**
     * Configuration for SNOWFLAKE ID strategy.
     */
    data class SnowflakeProperties(
        /**
         * Unique node ID to distinguish instances.
         */
        val nodeId: Long = 1,
        /**
         * Custom epoch timestamp in ISO-8601 format.
         * Example: "2024-01-01T00:00:00"
         */
        val customEpoch: String? = null,
    )
}
