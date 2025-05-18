package io.dopamine.demo.response

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@TestPropertySource(properties = ["dopamine.response.enabled=false"])
@AutoConfigureMockMvc
class ResponseDisabledTest(
    private val mockMvc: MockMvc,
) : FunSpec({
        test("자동 래핑 비활성화 시 원래 응답이 그대로 반환된다") {
            val result = mockMvc.get("/sample/string").andReturn()
            val content = result.response.contentAsString
            content shouldBe "hello-world"
        }
    })
