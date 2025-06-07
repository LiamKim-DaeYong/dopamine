package io.dopamine.test.support.assertion

import io.dopamine.response.common.model.DopamineResponse
import io.kotest.matchers.shouldBe
import java.time.format.DateTimeFormatter

data class ExpectedResponse<T>(
    val code: String,
    val message: String,
    val data: T?,
    val traceId: String? = null,
    val formatter: DateTimeFormatter? = null,
)

infix fun <T> DopamineResponse<T>.shouldBeSuccessWith(expected: ExpectedResponse<T>) {
    code shouldBe expected.code
    message shouldBe expected.message
    data shouldBe expected.data
    expected.formatter?.parse(timestamp)
    expected.traceId?.let { meta shouldContainTraceId it }
}
