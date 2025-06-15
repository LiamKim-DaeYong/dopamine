package io.dopamine.file.mvc.controller

import io.dopamine.file.common.model.StoredFile
import io.dopamine.file.mvc.service.FileUploadService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("\${dopamine.file.base-path:/files}")
class FileController(
    private val fileUploadService: FileUploadService,
) {
    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("storage", defaultValue = "default") storageName: String,
    ): StoredFile = fileUploadService.upload(file, storageName)

    @DeleteMapping("/{filename}")
    fun delete(
        @PathVariable filename: String,
        @RequestParam("storage", defaultValue = "default") storageName: String,
    ) {
        fileUploadService.delete(filename, storageName)
    }

    @GetMapping("/download")
    fun download(
        @RequestParam("path") path: String,
        @RequestParam("storage", defaultValue = "default") storageName: String,
        response: HttpServletResponse,
    ) {
        val download = fileUploadService.download(path, storageName)

        response.contentType = download.contentType?.toString() ?: "application/octet-stream"
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"${URLEncoder.encode(download.originalFilename, StandardCharsets.UTF_8)}\"",
        )
        response.outputStream.write(download.bytes)
    }

    @GetMapping("/preview")
    fun preview(
        @RequestParam path: String,
        @RequestParam(defaultValue = "default") storage: String,
    ): ResponseEntity<ByteArray> {
        val file = fileUploadService.download(path, storage)

        val contentType = file.contentType ?: MimeTypeUtils.APPLICATION_OCTET_STREAM
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType(contentType.toString()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${file.originalFilename}\"")
            .body(file.bytes)
    }
}
