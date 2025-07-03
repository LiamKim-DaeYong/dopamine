package io.dopamine.id.generator.strategy

import io.dopamine.id.generator.core.IdGenerator
import java.util.UUID

/**
 * Generates a UUID-based identifier.
 *
 * Useful for general-purpose ID generation with strong uniqueness guarantees.
 */
class UuidGenerator(
    private val upperCase: Boolean,
    private val withoutHyphen: Boolean,
) : IdGenerator {
    override fun generate(): String {
        var result = UUID.randomUUID().toString()
        if (withoutHyphen) result = result.replace("-", "")
        if (upperCase) result = result.uppercase()
        return result
    }
}
