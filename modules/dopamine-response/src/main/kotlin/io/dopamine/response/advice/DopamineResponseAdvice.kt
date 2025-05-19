package io.dopamine.response.advice

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.response.config.ResponseProperties
import io.dopamine.response.model.DopamineResponse
import org.slf4j.MDC
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.time.LocalDateTime

/**
 * API 응답 자동 포맷 Advice
 * - 응답을 DopamineResponse<T> 형태로 자동 래핑
 * - timestamp / meta(traceId 등) 자동 포함
 */
@RestControllerAdvice
class DopamineResponseAdvice(
    private val props: ResponseProperties,
    private val objectMapper: ObjectMapper,
) : ResponseBodyAdvice<Any> {
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean = true

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        // 1. reactive는 무시
        if (isReactiveType(body)) return body

        // 2. ResponseEntity는 자동 래핑 금지
        if (returnType.parameterType.name.contains("ResponseEntity")) return body

        // 3. 이미 래핑된 경우도 그대로 반환
        if (body is DopamineResponse<*>) return applyFormatting(body)

        val timestamp = LocalDateTime.now().format(props.timestampFormat.formatter())
        val meta = if (props.includeMeta) buildMeta() else null
        val wrapped = DopamineResponse.success(body, timestamp = timestamp, meta = meta)

        val isStringReturn = returnType.parameterType == String::class.java
        return if (isStringReturn) objectMapper.writeValueAsString(wrapped) else wrapped
    }

    private fun isReactiveType(body: Any?): Boolean =
        body != null && body::class.java.name.startsWith("reactor.core.publisher.")

    private fun buildMeta(): Map<String, Any> {
        val meta = mutableMapOf<String, Any>()
        if (props.metaOptions.includeTraceId) {
            val traceKey = props.metaOptions.traceIdKey
            MDC.get(traceKey)?.let { meta[traceKey] = it }
        }
        return meta
    }

    private fun <T> applyFormatting(body: DopamineResponse<T>): DopamineResponse<T> {
        val timestamp = LocalDateTime.now().format(props.timestampFormat.formatter())
        val meta = if (props.includeMeta) buildMeta() else body.meta
        return body.copy(timestamp = timestamp, meta = meta)
    }
}
