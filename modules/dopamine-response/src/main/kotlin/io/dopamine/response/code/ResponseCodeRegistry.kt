package io.dopamine.response.code

import io.dopamine.response.config.ResponseProperties
import org.springframework.stereotype.Component

@Component
class ResponseCodeRegistry(
    private val props: ResponseProperties,
) {
    private val messageOverrideMap: Map<String, String> =
        props.codes.associate {
            it.code to it.message
        }

    fun resolve(code: ResponseCode): ResponseCode {
        val overriddenMessage = messageOverrideMap[code.code]
        return if (overriddenMessage != null) {
            object : ResponseCode by code {
                override val message: String = overriddenMessage
            }
        } else {
            code
        }
    }
}
