package io.dopamine.file.common.exception

import io.dopamine.core.code.ResponseCode

/**
 * Exception thrown when file storage operations fail.
 * Wraps a [ResponseCode] and optional cause to support structured error handling.
 */
class FileStorageException(
    val code: ResponseCode,
    message: String = code.code,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
