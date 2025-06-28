package io.dopamine.response.mvc.advice

import com.fasterxml.jackson.databind.ObjectMapper
import io.dopamine.core.code.CommonSuccessCode
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.factory.DopamineResponseFactory
import io.dopamine.response.common.metadata.MetaContributor
import io.dopamine.response.common.model.DopamineResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * Intercepts all REST controller responses and wraps them in a standardized [DopamineResponse] format.
 * Metadata is automatically injected via registered [MetaContributor]s.
 */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class DopamineResponseAdvice(
    private val factory: DopamineResponseFactory,
    private val objectMapper: ObjectMapper,
    private val props: ResponseProperties,
) : ResponseBodyAdvice<Any> {
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
        if (request != null && isIgnoredPath(request)) return false

        val clazz = returnType.parameterType
        return !DopamineResponse::class.java.isAssignableFrom(clazz) &&
            !ResponseEntity::class.java.isAssignableFrom(clazz) &&
            !clazz.name.startsWith("reactor.core.publisher.")
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        contentType: MediaType,
        converterType: Class<out HttpMessageConverter<*>>,
        req: ServerHttpRequest,
        res: ServerHttpResponse,
    ): Any? {
        if (isReactive(body) || body is ResponseEntity<*>) return body

        return when (body) {
            is DopamineResponse<*> -> body
            else -> {
                val wrapped =
                    factory.success(
                        data = body,
                        responseCode = CommonSuccessCode.SUCCESS,
                    )
                if (returnType.parameterType == String::class.java) {
                    objectMapper.writeValueAsString(wrapped)
                } else {
                    wrapped
                }
            }
        }
    }

    private fun isReactive(body: Any?): Boolean {
        val className = body?.javaClass?.name ?: return false
        return className.startsWith("reactor.core.publisher.")
    }

    private fun isIgnoredPath(request: HttpServletRequest): Boolean {
        val uri = request.requestURI
        return props.ignorePaths.any { uri.startsWith(it) }
    }
}
