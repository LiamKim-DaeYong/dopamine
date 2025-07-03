package io.dopamine.id.generator.util

import java.time.LocalDateTime
import java.time.ZoneOffset

object SnowflakeEpochUtil {
    const val DEFAULT_EPOCH = 1704067200000L

    fun parseOrDefault(epochString: String?): Long =
        epochString?.let {
            try {
                LocalDateTime
                    .parse(it)
                    .atZone(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli()
            } catch (ex: Exception) {
                throw IllegalArgumentException(
                    "Invalid format for customEpoch: $it. Expected ISO-8601 format (e.g., 2024-01-01T00:00:00)",
                    ex,
                )
            }
        } ?: DEFAULT_EPOCH
}
