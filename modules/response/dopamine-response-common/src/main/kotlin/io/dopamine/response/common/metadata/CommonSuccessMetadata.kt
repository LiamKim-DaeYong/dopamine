package io.dopamine.response.common.metadata

import io.dopamine.core.code.CommonSuccessCode
import org.springframework.http.HttpStatus

/**
 * Static metadata mapping for [CommonSuccessCode].
 */
object CommonSuccessMetadata {
    val values: List<ResponseCodeMetadata> =
        listOf(
            ResponseCodeMetadata(
                code = CommonSuccessCode.SUCCESS.code,
                httpStatus = HttpStatus.OK,
                messageKey = "dopamine.success.200",
                defaultMessage = "Request was successful.",
            ),
            ResponseCodeMetadata(
                code = CommonSuccessCode.CREATED.code,
                httpStatus = HttpStatus.CREATED,
                messageKey = "dopamine.success.201",
                defaultMessage = "Resource has been created.",
            ),
            ResponseCodeMetadata(
                code = CommonSuccessCode.ACCEPTED.code,
                httpStatus = HttpStatus.ACCEPTED,
                messageKey = "dopamine.success.202",
                defaultMessage = "Request has been accepted.",
            ),
            ResponseCodeMetadata(
                code = CommonSuccessCode.NO_CONTENT.code,
                httpStatus = HttpStatus.NO_CONTENT,
                messageKey = "dopamine.success.204",
                defaultMessage = "No content.",
            ),
        )
}
