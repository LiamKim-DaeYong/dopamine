package io.dopamine.core.code

/**
 * A unified contract for representing business status codes.
 *
 * Designed for consistent use across different layers such as HTTP responses,
 * domain events, logs, and internal protocols within the Spring ecosystem.
 */
interface ResponseCode {
    val code: String
}
