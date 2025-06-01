package io.dopamine.test.support.util

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

object FixedClock {
    private val zone: ZoneId = ZoneId.systemDefault()

    fun fixed(isoInstant: String): Clock = Clock.fixed(Instant.parse(isoInstant), zone)

    fun now(clock: Clock = Clock.systemDefaultZone()): Instant = Instant.now(clock)

    fun nowAsLocalDateTime(clock: Clock = Clock.systemDefaultZone()): LocalDateTime =
        LocalDateTime.ofInstant(now(clock), zone)

    fun nowAsOffsetDateTime(clock: Clock = Clock.systemDefaultZone()): OffsetDateTime =
        OffsetDateTime.ofInstant(now(clock), zone)
}
