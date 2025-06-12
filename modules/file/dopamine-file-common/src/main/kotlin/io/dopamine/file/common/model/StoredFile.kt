package io.dopamine.file.common.model

import org.springframework.util.MimeType

/**
 * Represents a stored file's metadata after a successful upload.
 * Can be returned to the client or used internally for download/delete/etc.
 */
data class StoredFile(
    /**
     * Final stored file name (usually a generated name).
     * This may differ from the original filename for uniqueness.
     */
    val name: String,
    /**
     * Original file name received from the client during upload.
     */
    val originalName: String,
    /**
     * Full path or storage-specific key where the file is stored.
     * This is the value used to retrieve or delete the file.
     */
    val fullPath: String,
    /**
     * MIME type of the stored file.
     */
    val contentType: MimeType? = null,
    /**
     * Size of the stored file.
     */
    val size: FileSize,
)
