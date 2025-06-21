package io.dopamine.id.generator

import IdGenerator
import java.util.concurrent.atomic.AtomicLong

/**
 * Generates a time-based unique identifier using Snowflake algorithm.
 *
 * Suitable for sortable and distributed ID generation scenarios.
 */
class SnowflakeIdGenerator(
    private val nodeId: Long,
    private val customEpoch: Long = DEFAULT_EPOCH,
) : IdGenerator {
    private val sequence = AtomicLong(0L)
    private var lastTimestamp = -1L

    /**
     * Generates a Snowflake ID as a decimal string.
     * Use [generateHex] for short, URL-safe string representation.
     */
    override fun generate(): String = generateLong().toString()

    /**
     * Generates a Snowflake ID as a raw Long.
     */
    fun generateLong(): Long = synchronized(this) {
        var currentTimestamp = timestamp()

        check(currentTimestamp >= lastTimestamp) {
            "Clock moved backwards. Refusing to generate id."
        }

        if (currentTimestamp == lastTimestamp) {
            sequence.set((sequence.incrementAndGet()) and MAX_SEQUENCE)
            if (sequence.get() == 0L) {
                currentTimestamp = waitNextMillis(currentTimestamp)
            }
        } else {
            sequence.set(0L)
        }

        lastTimestamp = currentTimestamp

        return ((currentTimestamp - customEpoch) shl TIMESTAMP_SHIFT) or
            (nodeId shl NODE_ID_SHIFT) or
            sequence.get()
    }

    /**
     * Generates a Snowflake ID as a lowercase hexadecimal string.
     */
    fun generateHex(): String = generateLong().toString(16)

    private fun timestamp(): Long = System.currentTimeMillis()

    private fun waitNextMillis(currentTimestamp: Long): Long {
        var timestamp = timestamp()
        while (timestamp <= currentTimestamp) {
            timestamp = timestamp()
        }
        return timestamp
    }

    companion object {
        private const val NODE_ID_BITS = 10
        private const val SEQUENCE_BITS = 12

        private const val MAX_NODE_ID = -1L xor (-1L shl NODE_ID_BITS)
        private const val MAX_SEQUENCE = -1L xor (-1L shl SEQUENCE_BITS)

        private const val NODE_ID_SHIFT = SEQUENCE_BITS
        private const val TIMESTAMP_SHIFT = SEQUENCE_BITS + NODE_ID_BITS

        // Custom epoch: 2024-01-01T00:00:00Z
        private const val DEFAULT_EPOCH = 1704067200000L
    }
}
