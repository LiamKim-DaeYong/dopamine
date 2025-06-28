package io.dopamine.integration.mvc.test.response

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest
@AutoConfigureMockMvc
class ResponseWrappingTest(
    private val mockMvc: MockMvc,
) : BehaviorSpec({
        Given("a successful HTTP request") {
            When("the controller returns a response") {
                Then("the response should be wrapped with meta and data") {
                    mockMvc
                        .get("/test/ok") {
                            accept = MediaType.APPLICATION_JSON
                        }.andExpect {
                            status { isOk() }
                            jsonPath("$.code").value("SUCCESS")
                            jsonPath("$.message").exists()
                            jsonPath("$.data.message").value("pong")
                            jsonPath("$.meta.traceId").exists()
                            jsonPath("$.timestamp").exists()
                        }
                }
            }
        }

        Given("an HTTP request that throws an exception") {
            Then("the error response should be wrapped with meta and error code") {
                mockMvc
                    .get("/test/error") {
                        accept = MediaType.APPLICATION_JSON
                    }.andExpect {
                        status { is5xxServerError() }
                        jsonPath("$.code").value("INTERNAL_SERVER_ERROR")
                        jsonPath("$.message").exists()
                        jsonPath("$.data").doesNotExist()
                        jsonPath("$.meta.traceId").exists()
                        jsonPath("$.timestamp").exists()
                    }
            }
        }
    })
