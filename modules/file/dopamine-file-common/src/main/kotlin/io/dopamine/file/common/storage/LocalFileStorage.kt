package io.dopamine.file.common.storage

import io.dopamine.core.format.TimestampFormat
import io.dopamine.file.common.code.FileErrorCode
import io.dopamine.file.common.exception.FileStorageException
import io.dopamine.file.common.model.FileUploadRequest
import io.dopamine.file.common.model.StoredFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.UUID

/**
 * [FileStorage] implementation that saves files to the local file system.
 */
class LocalFileStorage(
    private val baseDir: Path,
    private val generateUniqueName: Boolean = true,
    private val useDateFolder: Boolean = true,
) : FileStorage {
    override fun upload(request: FileUploadRequest): StoredFile {
        try {
            val sanitizedName = sanitizeFilename(request.originalFilename)
            val extension = sanitizedName.substringAfterLast('.', missingDelimiterValue = "")
            val uniqueName =
                if (generateUniqueName) {
                    UUID.randomUUID().toString() + if (extension.isNotBlank()) ".$extension" else ""
                } else {
                    sanitizedName
                }

            val relativePath = buildRelativePath(uniqueName)
            val targetPath = baseDir.resolve(relativePath).normalize().toAbsolutePath()

            Files.createDirectories(targetPath.parent)
            Files.write(targetPath, request.bytes)

            return StoredFile(
                name = uniqueName,
                originalName = sanitizedName,
                fullPath = relativePath.toString().replace(File.separatorChar, '/'),
                contentType = request.contentType,
                size = request.size,
            )
        } catch (ex: IOException) {
            throw FileStorageException(
                code = FileErrorCode.STORAGE_FAILED,
                message = "Failed to store file: ${request.originalFilename}",
                cause = ex,
            )
        }
    }

    override fun download(storagePath: String): ByteArray {
        val path = resolvePath(storagePath)
        if (!Files.exists(path)) {
            throw FileStorageException(
                code = FileErrorCode.FILE_NOT_FOUND,
                message = "File not found: $storagePath",
            )
        }

        return try {
            Files.readAllBytes(path)
        } catch (ex: IOException) {
            throw FileStorageException(
                code = FileErrorCode.FILE_READ_ERROR,
                message = "Failed to read file: $storagePath",
                cause = ex,
            )
        }
    }

    override fun delete(storagePath: String) {
        val path = resolvePath(storagePath)

        if (!Files.exists(path)) return

        try {
            Files.delete(path)
        } catch (ex: IOException) {
            throw FileStorageException(
                code = FileErrorCode.FILE_DELETE_FAILED,
                message = "Failed to delete file: $storagePath",
                cause = ex,
            )
        }
    }

    override fun resolveUrl(storagePath: String): String = "file://${resolvePath(storagePath)}"

    override fun exists(storagePath: String): Boolean = Files.exists(resolvePath(storagePath))

    private fun resolvePath(relativePath: String): Path = baseDir.resolve(relativePath).normalize().toAbsolutePath()

    private fun sanitizeFilename(original: String): String = original.replace(Regex("[^a-zA-Z0-9_.-]"), "_")

    private fun buildRelativePath(fileName: String): Path =
        if (useDateFolder) {
            val formatter = TimestampFormat.COMPACT.formatter()
            val datePath = LocalDateTime.now().format(formatter)
            Path.of(datePath).resolve(fileName)
        } else {
            Path.of(fileName)
        }
}
