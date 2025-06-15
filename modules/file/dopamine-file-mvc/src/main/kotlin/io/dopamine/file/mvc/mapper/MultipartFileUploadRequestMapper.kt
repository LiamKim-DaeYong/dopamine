package io.dopamine.file.mvc.mapper

import io.dopamine.file.common.model.FileSize
import io.dopamine.file.common.model.FileUploadRequest
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile

/**
 * Converts [MultipartFile] into [FileUploadRequest].
 * Used in MVC-based file upload controller.
 */
class MultipartFileUploadRequestMapper {
    fun from(file: MultipartFile): FileUploadRequest =
        FileUploadRequest(
            originalFilename = file.originalFilename ?: "unknown",
            contentType = file.contentType?.let { MimeTypeUtils.parseMimeType(it) },
            size = FileSize(file.size),
            bytes = file.bytes,
        )
}
