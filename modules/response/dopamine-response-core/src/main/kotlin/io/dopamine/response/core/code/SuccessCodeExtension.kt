package io.dopamine.response.core.code

import io.dopamine.core.code.SuccessCode
import io.dopamine.core.code.SuccessCode.entries
import org.springframework.http.HttpStatus

fun SuccessCode.Companion.fromHttpStatus(httpStatus: HttpStatus): SuccessCode? =
    entries.find { it.httpStatus == httpStatus.value() }
