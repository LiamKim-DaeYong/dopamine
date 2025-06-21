package io.dopamine.id.generator

/**
 * Configuration holder for ID generator strategy and options.
 */
data class IdGeneratorProperties(
    val type: IdType = IdType.UUID,
    val nodeId: Long = 1
)
