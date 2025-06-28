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
class PagingMetaContributorTest(
    private val mockMvc: MockMvc,
) : BehaviorSpec({

        Given("a paginated endpoint") {
            When("a page is returned") {
                Then("the meta field should include paging information") {
                    mockMvc
                        .get("/test/page") {
                            accept = MediaType.APPLICATION_JSON
                        }.andExpect {
                            status { isOk() }
                            jsonPath("$.meta.paging").exists()
                            jsonPath("$.meta.paging.totalPages").value(4)
                            jsonPath("$.meta.paging.totalElements").value(10)
                            jsonPath("$.meta.paging.page").value(0)
                            jsonPath("$.meta.paging.size").value(3)
                        }
                }
            }
        }
    })
