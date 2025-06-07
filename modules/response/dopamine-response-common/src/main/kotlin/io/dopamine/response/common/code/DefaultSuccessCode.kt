package io.dopamine.response.common.code

import io.dopamine.core.code.ResponseCode
import org.springframework.http.HttpStatus

/**
 * Predefined success codes used in HTTP responses and Spring-based components.
 *
 * Includes HTTP status and message metadata for standardized formatting and localization.
 * Can be extended or overridden with custom ResponseCode implementations.
 */

enum class DefaultSuccessCode(
    override val code: String,
    val httpStatus: Int,
    val messageKey: String,
    val defaultMessage: String,
) : ResponseCode {
    SUCCESS("SUCCESS_200", 200, "dopamine.success.200", "Request was successful."),
    CREATED("SUCCESS_201", 201, "dopamine.success.201", "Resource has been created."),
    ACCEPTED("SUCCESS_202", 202, "dopamine.success.202", "Request has been accepted."),
    NO_CONTENT("SUCCESS_204", 204, "dopamine.success.204", "No content."),
    ;

    companion object {
        fun fromCode(code: String): DefaultSuccessCode? = entries.find { it.code == code }

        fun fromHttpStatus(httpStatus: HttpStatus): DefaultSuccessCode? =
            entries.find { it.httpStatus == httpStatus.value() }

        fun default(): DefaultSuccessCode = SUCCESS
    }
}
