package io.dopamine.response.common.exception

import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.factory.DopamineResponseFactory

/**
 * Base exception type used within Dopamine for explicitly handling known errors.
 *
 * This exception should be thrown in cases where the business logic can explicitly determine
 * the type of failure, such as validation failures, illegal states, etc.
 *
 * The [code] represents the reason for the error and is intended to be resolved by
 * [DopamineResponseFactory] to construct the appropriate error response.
 * Optionally, [arguments] can be supplied for message formatting during resolution.
 */
class DopamineException(
    val code: ResponseCode,
    val arguments: Array<Any>? = null,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)
