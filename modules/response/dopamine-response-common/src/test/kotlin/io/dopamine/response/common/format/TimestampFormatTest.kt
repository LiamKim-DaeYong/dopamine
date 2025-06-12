package io.dopamine.response.common.format

import io.dopamine.core.format.TimestampFormat
import io.dopamine.test.support.util.FixedClock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TimestampFormatTest :
    FunSpec({

        val dateTime = FixedClock.nowAsLocalDateTime()

        TimestampFormat.entries.forEach { format ->
            test("$format should format correctly") {
                val formatter = format.formatter()
                val formatted = formatter.format(dateTime)

                formatter.format(formatter.parse(formatted)) shouldBe formatted
            }
        }
    })
