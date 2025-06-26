package io.dopamine.file.common.storage

import io.dopamine.file.common.model.FileUploadRequest
import io.dopamine.file.common.model.StoredFile
import org.springframework.util.MimeType

/**
 * Core interface for file storage handling in the Dopamine framework.
 * Implementations may include local disk, AWS S3, or other custom storages.
 */
interface FileStorage {
    /**
     * Uploads a file to the target storage.
     *
     * @param request the abstracted upload request containing file content and metadata
     * @return metadata of the stored file after successful upload
     */
    fun upload(request: FileUploadRequest): StoredFile

    /**
     * Downloads a file from the storage by its full path or storage key.
     *
     * @param storagePath the full internal path or key used to locate the file
     * @return binary content of the file as ByteArray
     * @throws io.dopamine.file.common.exception.FileStorageException if the file does not exist or cannot be read
     */
    fun download(storagePath: String): ByteArray

    /**
     * Deletes a file from the storage by its full path or storage key.
     *
     * @param storagePath the internal path or key used to locate the file
     * @return true if the file was deleted, false if not found
     */
    fun delete(storagePath: String): Boolean

    /**
     * Resolves a public-accessible URL for a given stored file path, if supported.
     *
     * @param storagePath the internal path or key of the stored file
     * @return a fully qualified public URL to access the file
     * @throws UnsupportedOperationException if this storage does not support URL generation
     */
    fun resolveUrl(storagePath: String): String =
        throw UnsupportedOperationException("resolveUrl is not supported by this storage")

    /**
     * Checks whether the given file exists in the storage.
     *
     * @param storagePath the internal path or key used to locate the file
     * @return true if the file exists, false otherwise
     */
    fun exists(storagePath: String): Boolean

    /**
     * Attempts to detect the MIME type of a file by its internal storage path.
     *
     * This is useful when downloading files or generating preview links that require accurate content type information.
     * Implementations may use OS-level detection (e.g., Files.probeContentType) or storage-specific metadata (e.g., S3 headObject).
     *
     * @param storagePath the internal path or key used to locate the file
     * @return a [MimeType] representing the file's content type, or null if unknown or detection is unsupported
     * @throws io.dopamine.file.common.exception.FileStorageException if an error occurs during detection
     */
    fun detectContentType(storagePath: String): MimeType? =
        throw UnsupportedOperationException("detectContentType is not supported by this storage")
}
