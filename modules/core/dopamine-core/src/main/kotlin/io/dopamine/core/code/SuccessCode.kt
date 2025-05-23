package io.dopamine.core.code

/**
 * Default success code definitions used across the application.
 * These can be overridden by external configuration.
 */
enum class SuccessCode(
    val code: String,
    val defaultMessage: String,
) {
    SUCCESS("SUCCESS_200", "Request was successful."),
    CREATED("SUCCESS_201", "Resource has been created."),
    ACCEPTED("SUCCESS_202", "Request has been accepted."),
    NO_CONTENT("SUCCESS_204", "No content."),
    ;

    companion object {
        fun fromCode(code: String): SuccessCode? = SuccessCode.entries.find { it.code == code }
    }
}
