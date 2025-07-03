package io.dopamine.id.generator.core

/**
 * Supported ID generation strategies.
 */
enum class IdType {
    UUID,
    SNOWFLAKE,
    HASHED,
    CUSTOM,
}
