package io.dopamine.core.code

/**
 * Default success code definitions used across the application.
 * These can be overridden by external configuration.
 */

private const val PREFIX = "SUCCESS"

enum class SuccessCode(
    val httpStatus: Int,
    val code: String,
    val defaultMessage: String,
    val messageKey: String,
) {
    SUCCESS(200, "${PREFIX}_200", "Request was successful.", "dopamine.success.200"),
    CREATED(201, "${PREFIX}_201", "Resource has been created.", "dopamine.success.201"),
    ACCEPTED(202, "${PREFIX}_202", "Request has been accepted.", "dopamine.success.202"),
    NO_CONTENT(204, "${PREFIX}_204", "No content.", "dopamine.success.204"),
    ;

    companion object {
        fun fromCode(code: String): SuccessCode? = entries.find { it.code == code }

        fun fromHttpStatusCode(code: Int): SuccessCode? = entries.find { it.code == "${PREFIX}_$code" }

        fun default(): SuccessCode = SUCCESS
    }
}
