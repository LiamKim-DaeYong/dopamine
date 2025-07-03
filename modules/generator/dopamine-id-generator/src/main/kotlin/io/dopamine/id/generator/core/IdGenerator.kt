package io.dopamine.id.generator.core

/**
 * Generates a unique identifier.
 *
 * Implementations can use UUID, Snowflake, or other ID generation strategies.
 */
fun interface IdGenerator {
    fun generate(): String
}
