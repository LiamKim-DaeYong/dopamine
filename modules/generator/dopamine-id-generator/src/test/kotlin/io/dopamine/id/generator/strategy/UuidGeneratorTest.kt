package io.dopamine.id.generator.strategy

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch

class UuidGeneratorTest :
    FunSpec({
        test("should generate UUID with default format") {
            val generator =
                UuidGenerator(
                    upperCase = false,
                    withoutHyphen = false,
                )
            val id = generator.generate()
            id shouldMatch Regex("^[a-f0-9\\-]{36}$")
        }

        test("should generate uppercase UUID without hyphens") {
            val generator = UuidGenerator(upperCase = true, withoutHyphen = true)
            val id = generator.generate()
            id shouldMatch Regex("^[A-F0-9]{32}$")
        }

        test("should generate unique UUIDs") {
            val generator =
                UuidGenerator(
                    upperCase = false,
                    withoutHyphen = false,
                )
            val ids = (1..1000).map { generator.generate() }.toSet()
            ids.size shouldBe 1000
        }
    })
