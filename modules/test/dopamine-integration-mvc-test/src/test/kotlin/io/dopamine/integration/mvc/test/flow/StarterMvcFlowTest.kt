package io.dopamine.integration.mvc.test.flow

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest
@AutoConfigureMockMvc
class StarterMvcFlowTest(
    private val mockMvc: MockMvc,
) : BehaviorSpec({
        Given("a successful HTTP request") {
            When("the controller returns a response") {
                Then("the response should include meta.traceId") {
                    mockMvc
                        .get("/test/ok") {
                            accept = MediaType.APPLICATION_JSON
                        }.andExpect {
                            status { isOk() }
                            jsonPath("$.meta.traceId").exists()
                            jsonPath("$.data.message").value("pong")
                        }
                }
            }
        }

        Given("an HTTP request that throws an exception") {
            Then("the response should be wrapped and include meta.traceId with null data") {
                mockMvc
                    .get("/test/error") {
                        accept = MediaType.APPLICATION_JSON
                    }.andExpect {
                        status { is5xxServerError() }
                        jsonPath("$.meta.traceId").exists()
                        jsonPath("$.data").doesNotExist()
                    }
            }
        }
    })
