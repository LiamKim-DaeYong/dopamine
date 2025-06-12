package io.dopamine.file.common.code

import io.dopamine.core.code.ResponseCode

/**
 * Predefined response codes for file-related errors.
 * Used in DopamineResponse as code/message pairs.
 */
enum class FileErrorCode(
    override val code: String,
    val messageKey: String,
    val defaultMessage: String,
) : ResponseCode {
    FILE_NOT_FOUND("FILE404", "file.error.not-found", "File not found."),
    STORAGE_FAILED("FILE500", "file.error.storage-failed", "Failed to store file."),
    FILE_READ_ERROR("FILE502", "file.error.read-failed", "Failed to read file."),
    FILE_DELETE_FAILED("FILE503", "file.error.delete-failed", "Failed to delete file."),
    INVALID_EXTENSION("FILE400", "file.error.invalid-extension", "Invalid file extension."),
    SIZE_LIMIT_EXCEEDED("FILE413", "file.error.size-limit", "File size exceeds the allowed limit."),
}
