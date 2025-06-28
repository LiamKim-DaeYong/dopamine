package io.dopamine.response.common.metadata

import io.dopamine.core.code.CommonErrorCode
import org.springframework.http.HttpStatus

/**
 * Static metadata mapping for [CommonErrorCode].
 */
object CommonErrorMetadata {
    val values: List<ResponseCodeMetadata> =
        listOf(
            // fallback
            ResponseCodeMetadata(
                code = CommonErrorCode.BAD_REQUEST.code,
                httpStatus = HttpStatus.BAD_REQUEST,
                messageKey = "dopamine.error.400",
                defaultMessage = "Bad request.",
            ),
            // validation
            ResponseCodeMetadata(
                code = CommonErrorCode.INVALID_REQUEST.code,
                httpStatus = HttpStatus.BAD_REQUEST,
                messageKey = "dopamine.error.valid.400",
                defaultMessage = "The request is invalid.",
            ),
            // domain
            ResponseCodeMetadata(
                code = CommonErrorCode.RESOURCE_NOT_FOUND.code,
                httpStatus = HttpStatus.NOT_FOUND,
                messageKey = "dopamine.error.domain.404",
                defaultMessage = "The requested resource was not found.",
            ),
            ResponseCodeMetadata(
                code = CommonErrorCode.RESOURCE_ALREADY_EXISTS.code,
                httpStatus = HttpStatus.CONFLICT,
                messageKey = "dopamine.error.domain.409",
                defaultMessage = "The resource already exists.",
            ),
            ResponseCodeMetadata(
                code = CommonErrorCode.BUSINESS_RULE_VIOLATION.code,
                httpStatus = HttpStatus.BAD_REQUEST,
                messageKey = "dopamine.error.domain.400.rule",
                defaultMessage = "Business rule was violated.",
            ),
            // external
            ResponseCodeMetadata(
                code = CommonErrorCode.EXTERNAL_TIMEOUT.code,
                httpStatus = HttpStatus.GATEWAY_TIMEOUT,
                messageKey = "dopamine.error.external.504",
                defaultMessage = "External system timeout.",
            ),
            ResponseCodeMetadata(
                code = CommonErrorCode.EXTERNAL_ERROR.code,
                httpStatus = HttpStatus.BAD_GATEWAY,
                messageKey = "dopamine.error.external.502",
                defaultMessage = "External system error.",
            ),
            // system
            ResponseCodeMetadata(
                code = CommonErrorCode.INTERNAL_SERVER_ERROR.code,
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                messageKey = "dopamine.error.sys.500",
                defaultMessage = "Internal server error.",
            ),
            ResponseCodeMetadata(
                code = CommonErrorCode.SERVICE_UNAVAILABLE.code,
                httpStatus = HttpStatus.SERVICE_UNAVAILABLE,
                messageKey = "dopamine.error.sys.503",
                defaultMessage = "Service is temporarily unavailable.",
            ),
        )
}
