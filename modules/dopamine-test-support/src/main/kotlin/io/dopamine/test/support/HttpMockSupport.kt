package io.dopamine.test.support

import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

fun mockRequest() = ServletServerHttpRequest(MockHttpServletRequest())

fun mockResponse() = ServletServerHttpResponse(MockHttpServletResponse())
