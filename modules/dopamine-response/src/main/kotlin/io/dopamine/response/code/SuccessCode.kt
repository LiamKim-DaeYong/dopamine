package io.dopamine.response.code

import org.springframework.http.HttpStatus

/**
 * 공통 성공 응답 코드
 */
enum class SuccessCode(
    override val code: String,
    override val message: String,
    override val httpStatus: HttpStatus,
) : ResponseCode {
    SUCCESS(HttpStatus.OK),
    CREATED(HttpStatus.CREATED),
    ACCEPTED(HttpStatus.ACCEPTED),
    NO_CONTENT(HttpStatus.NO_CONTENT);

    constructor(httpStatus: HttpStatus) : this(
        code = httpStatus.name,
        message = httpStatus.reasonPhrase,
        httpStatus = httpStatus,
    )
}
