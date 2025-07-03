package io.dopamine.id.generator.strategy

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch

class HashedIdGeneratorTest :
    FunSpec({

        test("should generate ID with fixed length") {
            val generator = HashedIdGenerator(length = 10)
            val id = generator.generate()
            id.length shouldBe 10
        }

        test("should use custom alphabet") {
            val customAlphabet = "ABC123"
            val generator = HashedIdGenerator(length = 8, alphabet = customAlphabet)
            val id = generator.generate()
            id shouldMatch Regex("^[ABC123]+$")
        }

        test("should generate unique hashed IDs") {
            val generator = HashedIdGenerator(length = 12)
            val ids = (1..1000).map { generator.generate() }.toSet()
            ids.size shouldBe 1000
        }
    })
