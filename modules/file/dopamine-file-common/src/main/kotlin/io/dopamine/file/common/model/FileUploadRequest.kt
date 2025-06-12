package io.dopamine.file.common.model

import org.springframework.util.MimeType

/**
 * Represents a file upload request in an abstracted, Spring-friendly format.
 * Web-specific types like MultipartFile or PartData should be converted to this class
 * before being passed into a FileStorage implementation.
 */
data class FileUploadRequest(
    /**
     * Original file name provided by the client during upload.
     */
    val originalFilename: String,
    /**
     * MIME type of the file.
     * Can be used for validation, routing, or storage decisions.
     */
    val contentType: MimeType? = null,
    /**
     * Size of the file in bytes.
     */
    val size: FileSize,
    /**
     * Actual binary content of the file.
     */
    val bytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileUploadRequest

        if (originalFilename != other.originalFilename) return false
        if (contentType != other.contentType) return false
        if (size != other.size) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = originalFilename.hashCode()
        result = 31 * result + (contentType?.hashCode() ?: 0)
        result = 31 * result + size.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
