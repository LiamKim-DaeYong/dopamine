package io.dopamine.id.generator.strategy

import io.dopamine.id.generator.util.SnowflakeEpochUtil.DEFAULT_EPOCH
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe

class SnowflakeIdGeneratorTest :
    FunSpec({

        val generator =
            SnowflakeIdGenerator(
                nodeId = 1,
                customEpoch = DEFAULT_EPOCH,
            )

        test("should generate unique IDs") {
            val id1 = generator.generate().toLong()
            val id2 = generator.generate().toLong()

            id1 shouldNotBe id2
            id2 shouldBeGreaterThan id1
        }

        test("should generate IDs in increasing order") {
            val ids = (1..1000).map { generator.generate().toLong() }
            ids.zipWithNext().all { (a, b) -> b > a }.shouldBeTrue()
        }

        test("should generate valid hex IDs") {
            val hex = generator.generateHex()
            hex shouldNotBe null
            hex.matches(Regex("^[0-9a-f]+$")).shouldBeTrue()
            (hex.length in 8..16).shouldBeTrue()
        }

        test("should generate increasing long IDs") {
            val longs = (1..500).map { generator.generateLong() }
            longs.zipWithNext().all { (a, b) -> b > a }.shouldBeTrue()
        }
    })
