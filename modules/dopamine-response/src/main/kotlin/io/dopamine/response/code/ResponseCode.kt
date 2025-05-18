package io.dopamine.response.code

import org.springframework.http.HttpStatus

/**
 * 공통 응답 코드 인터페이스
 * 모든 성공/실패 응답 코드는 이 인터페이스를 통해 관리됨
 */
interface ResponseCode {
    val code: String
    val message: String
    val httpStatus: HttpStatus
}
