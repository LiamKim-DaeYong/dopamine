package io.dopamine.test.support.util

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

object FixedClock {
    private val defaultZone: ZoneId = ZoneId.systemDefault()

    fun fixed(
        instant: Instant,
        zoneId: ZoneId = defaultZone,
    ): Clock = Clock.fixed(instant, zoneId)

    fun fixed(
        isoInstant: String,
        zoneId: ZoneId = defaultZone,
    ): Clock = Clock.fixed(Instant.parse(isoInstant), zoneId)

    fun now(clock: Clock = Clock.systemDefaultZone()): Instant = Instant.now(clock)

    fun nowAsLocalDateTime(
        clock: Clock = Clock.systemDefaultZone(),
        zoneId: ZoneId = defaultZone,
    ): LocalDateTime = LocalDateTime.ofInstant(now(clock), zoneId)

    fun nowAsOffsetDateTime(
        clock: Clock = Clock.systemDefaultZone(),
        zoneId: ZoneId = defaultZone,
    ): OffsetDateTime = OffsetDateTime.ofInstant(now(clock), zoneId)
}
