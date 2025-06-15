package io.dopamine.file.mvc.service

import io.dopamine.file.common.model.FileDownload
import io.dopamine.file.common.model.StoredFile
import io.dopamine.file.common.resolve.FileStorageResolver
import io.dopamine.file.mvc.mapper.MultipartFileUploadRequestMapper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.FileNotFoundException
import java.nio.file.Path

interface FileUploadService {
    fun upload(
        file: MultipartFile,
        storageName: String,
    ): StoredFile

    fun delete(
        filename: String,
        storageName: String,
    ): Boolean

    fun download(
        path: String,
        storageName: String,
    ): FileDownload
}

@Service
class DefaultFileUploadService(
    private val storageResolver: FileStorageResolver,
    private val mapper: MultipartFileUploadRequestMapper,
) : FileUploadService {
    override fun upload(
        file: MultipartFile,
        storageName: String,
    ): StoredFile {
        val request = mapper.from(file)
        return storageResolver.resolve(storageName).upload(request)
    }

    override fun delete(
        filename: String,
        storageName: String,
    ): Boolean {
        val deleted = storageResolver.resolve(storageName).delete(filename)
        if (!deleted) {
            throw FileNotFoundException("File [$filename] not found in [$storageName]")
        }
        return true
    }

    override fun download(
        path: String,
        storageName: String,
    ): FileDownload {
        val storage = storageResolver.resolve(storageName)
        val bytes = storage.download(path)
        val contentType = storage.detectContentType(path)
        val originalName = Path.of(path).fileName.toString()

        return FileDownload(
            bytes = bytes,
            originalFilename = originalName,
            contentType = contentType,
        )
    }
}
