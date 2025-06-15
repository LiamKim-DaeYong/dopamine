package io.dopamine.file.common.model

import org.springframework.util.MimeType

/**
 * Represents a file download response.
 * Contains the binary content, original filename, and MIME type of the file.
 *
 * This model is used to return downloadable file information from service layer to the controller.
 */
data class FileDownload(
    /**
     * Actual binary content of the file.
     */
    val bytes: ByteArray,
    /**
     * Original filename that will be suggested to the client.
     * Used in Content-Disposition header during download.
     */
    val originalFilename: String,
    /**
     * MIME type of the file (e.g., image/png, application/pdf).
     * Helps the client interpret or preview the content properly.
     */
    val contentType: MimeType? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileDownload

        if (!bytes.contentEquals(other.bytes)) return false
        if (originalFilename != other.originalFilename) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + originalFilename.hashCode()
        result = 31 * result + (contentType?.hashCode() ?: 0)
        return result
    }
}
