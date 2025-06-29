package io.dopamine.response.common.metadata

import io.dopamine.core.code.CommonErrorCode
import io.dopamine.core.code.ResponseCode
import org.springframework.http.HttpStatus

/**
 * Provides metadata for built-in error codes.
 */
class CommonErrorMetadataProvider : ResponseMetadataProvider {
    private val metadataMap: Map<String, ResponseCodeMetadata> =
        mapOf(
            // fallback
            CommonErrorCode.BAD_REQUEST.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.BAD_REQUEST.code,
                    httpStatus = HttpStatus.BAD_REQUEST,
                    messageKey = "dopamine.error.400",
                    defaultMessage = "Bad request.",
                ),
            // validation
            CommonErrorCode.INVALID_REQUEST.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.INVALID_REQUEST.code,
                    httpStatus = HttpStatus.BAD_REQUEST,
                    messageKey = "dopamine.error.valid.400",
                    defaultMessage = "The request is invalid.",
                ),
            // domain
            CommonErrorCode.RESOURCE_NOT_FOUND.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.RESOURCE_NOT_FOUND.code,
                    httpStatus = HttpStatus.NOT_FOUND,
                    messageKey = "dopamine.error.domain.404",
                    defaultMessage = "The requested resource was not found.",
                ),
            CommonErrorCode.RESOURCE_ALREADY_EXISTS.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.RESOURCE_ALREADY_EXISTS.code,
                    httpStatus = HttpStatus.CONFLICT,
                    messageKey = "dopamine.error.domain.409",
                    defaultMessage = "The resource already exists.",
                ),
            CommonErrorCode.BUSINESS_RULE_VIOLATION.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.BUSINESS_RULE_VIOLATION.code,
                    httpStatus = HttpStatus.BAD_REQUEST,
                    messageKey = "dopamine.error.domain.400.rule",
                    defaultMessage = "Business rule was violated.",
                ),
            // external
            CommonErrorCode.EXTERNAL_TIMEOUT.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.EXTERNAL_TIMEOUT.code,
                    httpStatus = HttpStatus.GATEWAY_TIMEOUT,
                    messageKey = "dopamine.error.external.504",
                    defaultMessage = "External system timeout.",
                ),
            CommonErrorCode.EXTERNAL_ERROR.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.EXTERNAL_ERROR.code,
                    httpStatus = HttpStatus.BAD_GATEWAY,
                    messageKey = "dopamine.error.external.502",
                    defaultMessage = "External system error.",
                ),
            // system
            CommonErrorCode.INTERNAL_SERVER_ERROR.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.INTERNAL_SERVER_ERROR.code,
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                    messageKey = "dopamine.error.sys.500",
                    defaultMessage = "Internal server error.",
                ),
            CommonErrorCode.SERVICE_UNAVAILABLE.code to
                ResponseCodeMetadata(
                    code = CommonErrorCode.SERVICE_UNAVAILABLE.code,
                    httpStatus = HttpStatus.SERVICE_UNAVAILABLE,
                    messageKey = "dopamine.error.sys.503",
                    defaultMessage = "Service is temporarily unavailable.",
                ),
        )

    override fun supports(code: ResponseCode): Boolean = metadataMap.containsKey(code.code)

    override fun provide(code: ResponseCode): ResponseCodeMetadata =
        metadataMap[code.code]
            ?: throw IllegalArgumentException("No error metadata for code: ${code.code}")
}
