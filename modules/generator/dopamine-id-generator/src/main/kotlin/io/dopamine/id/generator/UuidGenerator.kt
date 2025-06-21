package io.dopamine.id.generator

import IdGenerator
import java.util.UUID

/**
 * Generates a UUID-based identifier.
 *
 * Useful for general-purpose ID generation with strong uniqueness guarantees.
 */
class UuidGenerator : IdGenerator {
    override fun generate(): String = UUID.randomUUID().toString()
}
