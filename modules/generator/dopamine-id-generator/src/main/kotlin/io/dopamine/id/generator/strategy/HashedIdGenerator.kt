package io.dopamine.id.generator.strategy

import io.dopamine.id.generator.core.IdGenerator
import java.math.BigInteger
import java.util.*

/**
 * Generates a fixed-length hashed ID using Base62-style encoding.
 *
 * Internally uses UUID as a source of entropy, and encodes it into
 * a fixed-length string using a custom or default alphabet.
 *
 * This strategy is useful when you need short, non-sequential,
 * URL-safe identifiers (e.g., public-facing keys, slugs).
 */
class HashedIdGenerator(
    private val length: Int = 12,
    private val alphabet: String? = null,
) : IdGenerator {
    private val chars: CharArray = (alphabet ?: DEFAULT_ALPHABET).toCharArray()
    private val base = chars.size

    override fun generate(): String {
        val uuid = UUID.randomUUID()
        val number = BigInteger(uuid.toString().replace("-", ""), 16)

        val encoded = StringBuilder()
        var n = number

        while (n > BigInteger.ZERO && encoded.length < length) {
            val idx = n.mod(BigInteger.valueOf(base.toLong())).toInt()
            encoded.append(chars[idx])
            n = n.divide(BigInteger.valueOf(base.toLong()))
        }

        return encoded.toString().padStart(length, chars[0])
    }

    companion object {
        /**
         * Default alphabet: Base62 (a-z, A-Z, 0-9).
         */
        private const val DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    }
}
