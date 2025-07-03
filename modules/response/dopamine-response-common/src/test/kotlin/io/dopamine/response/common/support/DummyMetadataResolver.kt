package io.dopamine.response.common.support

import io.dopamine.core.code.CommonErrorCode
import io.dopamine.core.code.CommonSuccessCode
import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.metadata.ResponseCodeMetadata
import io.dopamine.response.common.metadata.ResponseMetadataResolver
import org.springframework.http.HttpStatus

class DummyMetadataResolver(
    predefined: List<ResponseCodeMetadata> =
        listOf(
            ResponseCodeMetadata(
                code = CommonSuccessCode.SUCCESS.code,
                httpStatus = HttpStatus.OK,
                messageKey = "dopamine.success.200",
                defaultMessage = "Request was successful.",
            ),
            ResponseCodeMetadata(
                code = CommonErrorCode.BAD_REQUEST.code,
                httpStatus = HttpStatus.BAD_REQUEST,
                messageKey = "dopamine.error.400",
                defaultMessage = "Bad request.",
            ),
            ResponseCodeMetadata(
                code = CommonErrorCode.INVALID_REQUEST.code,
                httpStatus = HttpStatus.BAD_REQUEST,
                messageKey = "dopamine.error.valid.400",
                defaultMessage = "The request is invalid.",
            ),
            ResponseCodeMetadata(
                code = CommonErrorCode.INTERNAL_SERVER_ERROR.code,
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                messageKey = "dopamine.error.500",
                defaultMessage = "An unexpected error occurred.",
            ),
        ),
) : ResponseMetadataResolver {
    private val map: Map<String, ResponseCodeMetadata> = predefined.associateBy { it.code }

    override fun resolve(code: ResponseCode): ResponseCodeMetadata =
        map[code.code]
            ?: throw IllegalStateException("No metadata for code: ${code.code}")
}
