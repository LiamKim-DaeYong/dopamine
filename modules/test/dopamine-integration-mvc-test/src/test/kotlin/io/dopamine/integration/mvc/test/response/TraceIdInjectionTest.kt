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
class TraceIdInjectionTest(
    private val mockMvc: MockMvc,
) : BehaviorSpec({

        Given("a request with custom X-Trace-Id header") {
            Then("the traceId should match the provided header value") {
                mockMvc
                    .get("/test/ok") {
                        accept = MediaType.APPLICATION_JSON
                        header("X-Trace-Id", "trace-xyz-1234")
                    }.andExpect {
                        status { isOk() }
                        jsonPath("$.meta.traceId").value("trace-xyz-1234")
                    }
            }
        }

        Given("a request without X-Trace-Id header") {
            Then("the traceId should be auto-generated and not null or blank") {
                mockMvc
                    .get("/test/ok") {
                        accept = MediaType.APPLICATION_JSON
                    }.andExpect {
                        status { isOk() }
                        jsonPath("$.meta.traceId").isNotEmpty()
                    }
            }
        }
    })
