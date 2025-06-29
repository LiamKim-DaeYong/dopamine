package io.dopamine.response.common.support

import io.dopamine.core.code.ResponseCode
import io.dopamine.response.common.metadata.ResponseCodeMetadata
import io.dopamine.response.common.metadata.ResponseMetadataResolver
import org.springframework.http.HttpStatus

class DummyMetadataResolver(
    predefined: List<ResponseCodeMetadata> =
        listOf(
            ResponseCodeMetadata(
                code = "SUCCESS",
                httpStatus = HttpStatus.OK,
                messageKey = "dopamine.success.200",
                defaultMessage = "Request was successful.",
            ),
            ResponseCodeMetadata(
                code = "INVALID_REQUEST",
                httpStatus = HttpStatus.BAD_REQUEST,
                messageKey = "dopamine.error.valid.400",
                defaultMessage = "The request is invalid.",
            ),
            ResponseCodeMetadata(
                code = "INTERNAL_SERVER_ERROR",
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                messageKey = "dopamine.error.sys.500",
                defaultMessage = "Internal server error.",
            ),
        ),
) : ResponseMetadataResolver {
    private val map: Map<String, ResponseCodeMetadata> = predefined.associateBy { it.code }

    override fun resolve(code: ResponseCode): ResponseCodeMetadata =
        map[code.code]
            ?: throw IllegalStateException("No metadata for code: ${code.code}")
}
