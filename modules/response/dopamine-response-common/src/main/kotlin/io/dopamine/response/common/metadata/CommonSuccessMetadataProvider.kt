package io.dopamine.response.common.metadata

import io.dopamine.core.code.CommonSuccessCode
import io.dopamine.core.code.ResponseCode
import org.springframework.http.HttpStatus

/**
 * Provides metadata for built-in success codes.
 */
class CommonSuccessMetadataProvider : ResponseMetadataProvider {
    private val metadataMap: Map<String, ResponseCodeMetadata> =
        mapOf(
            CommonSuccessCode.SUCCESS.code to
                ResponseCodeMetadata(
                    code = CommonSuccessCode.SUCCESS.code,
                    httpStatus = HttpStatus.OK,
                    messageKey = "dopamine.success.200",
                    defaultMessage = "Request was successful.",
                ),
            CommonSuccessCode.CREATED.code to
                ResponseCodeMetadata(
                    code = CommonSuccessCode.CREATED.code,
                    httpStatus = HttpStatus.CREATED,
                    messageKey = "dopamine.success.201",
                    defaultMessage = "Resource has been created.",
                ),
            CommonSuccessCode.ACCEPTED.code to
                ResponseCodeMetadata(
                    code = CommonSuccessCode.ACCEPTED.code,
                    httpStatus = HttpStatus.ACCEPTED,
                    messageKey = "dopamine.success.202",
                    defaultMessage = "Request has been accepted.",
                ),
            CommonSuccessCode.NO_CONTENT.code to
                ResponseCodeMetadata(
                    code = CommonSuccessCode.NO_CONTENT.code,
                    httpStatus = HttpStatus.NO_CONTENT,
                    messageKey = "dopamine.success.204",
                    defaultMessage = "No content.",
                ),
        )

    override fun supports(code: ResponseCode): Boolean = metadataMap.containsKey(code.code)

    override fun provide(code: ResponseCode): ResponseCodeMetadata =
        metadataMap[code.code]
            ?: throw IllegalArgumentException("No success metadata for code: ${code.code}")
}
