package io.dopamine.test.support.assertions

import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

object ResponseAssertions {
    fun assertSuccess(result: ResultActions) {
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.meta.code").value("SUCCESS"))
            .andExpect(jsonPath("$.meta.message").exists())
            .andExpect(jsonPath("$.data").exists())
    }

    fun assertErrorResponse(
        result: ResultActions,
        expectedCode: String,
    ) {
        result
            .andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$.meta.code").value(expectedCode))
            .andExpect(jsonPath("$.data").doesNotExist())
    }

    fun assertTraceIdPresent(result: ResultActions) {
        result.andExpect(jsonPath("$.meta.traceId").exists())
    }
}
