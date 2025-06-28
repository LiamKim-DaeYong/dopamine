package io.dopamine.response.common.support

import io.dopamine.response.common.model.DopamineResponse
import io.kotest.matchers.shouldBe
import java.time.format.DateTimeFormatter

data class ExpectedResponse<T>(
    val code: String,
    val message: String,
    val data: T?,
    val formatter: DateTimeFormatter? = null,
    val meta: Map<String, Any?>? = null,
)

infix fun <T> DopamineResponse<T>.shouldBeSuccessWith(expected: ExpectedResponse<T>) {
    code shouldBe expected.code
    message shouldBe expected.message
    data shouldBe expected.data

    expected.formatter?.parse(timestamp)
    expected.meta?.forEach { (key, value) ->
        meta?.get(key) shouldBe value
    }
}
