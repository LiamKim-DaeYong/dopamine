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
    SUCCESS("SUCCESS_200", "요청이 성공적으로 처리되었습니다.", HttpStatus.OK),
    CREATED("SUCCESS_201", "리소스가 생성되었습니다.", HttpStatus.CREATED),
    ACCEPTED("SUCCESS_202", "요청이 수락되었습니다.", HttpStatus.ACCEPTED),
    NO_CONTENT("SUCCESS_204", "내용 없음 (성공)", HttpStatus.NO_CONTENT),
}
