package io.dopamine.test.support.mockmvc

import io.dopamine.test.support.assertions.ResponseAssertions
import org.springframework.test.web.servlet.ResultActions

fun ResultActions.andExpectSuccess(): ResultActions {
    ResponseAssertions.assertSuccess(this)
    return this
}

fun ResultActions.andExpectError(expectedCode: String): ResultActions {
    ResponseAssertions.assertErrorResponse(this, expectedCode)
    return this
}

fun ResultActions.andExpectTraceId(): ResultActions {
    ResponseAssertions.assertTraceIdPresent(this)
    return this
}

fun ResultActions.andPrintPretty(): ResultActions {
    println(this.andReturn().response.contentAsString)
    return this
}
