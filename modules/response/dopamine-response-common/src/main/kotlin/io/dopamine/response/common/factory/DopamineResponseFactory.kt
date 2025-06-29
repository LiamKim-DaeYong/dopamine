package io.dopamine.response.common.factory

import io.dopamine.core.code.CommonSuccessCode
import io.dopamine.core.code.ResponseCode
import io.dopamine.core.resolver.MessageResolver
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.metadata.MetaContributor
import io.dopamine.response.common.metadata.ResponseCodeMetadata
import io.dopamine.response.common.metadata.ResponseMetadataResolver
import io.dopamine.response.common.model.DopamineResponse
import org.slf4j.LoggerFactory
import java.time.Clock

/**
 * Factory responsible for constructing standardized DopamineResponse<T> instances.
 * Formatting, timestamp generation, and response code/message resolution are handled here.
 * Optional metadata contributors can enrich the meta field with additional context (e.g., traceId).
 */
class DopamineResponseFactory(
    private val props: ResponseProperties,
    private val resolver: ResponseMetadataResolver,
    private val messageResolver: MessageResolver,
    private val contributors: List<MetaContributor> = emptyList(),
    private val clock: Clock = Clock.systemDefaultZone(),
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun <T> success(
        data: T?,
        responseCode: ResponseCode = CommonSuccessCode.SUCCESS,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<T> = buildResponse(responseCode, data, meta)

    fun fail(
        responseCode: ResponseCode,
        meta: Map<String, Any>? = null,
        requestData: Any? = null,
    ): DopamineResponse<Any?> {
        val metaWithRequest =
            if (requestData != null) {
                (meta ?: emptyMap()) + mapOf("request" to requestData)
            } else {
                meta
            }
        return buildResponse(responseCode, null, metaWithRequest)
    }

    fun <T> of(
        data: T?,
        responseCode: ResponseCode,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<T> = buildResponse(responseCode, data, meta)

    private fun <T> buildResponse(
        responseCode: ResponseCode,
        data: T?,
        meta: Map<String, Any>? = null,
    ): DopamineResponse<T> {
        val (code, message) = resolveMessage(responseCode)
        val mergedMeta = if (props.includeMeta) mergeMeta(meta, data) else null

        return DopamineResponse(
            code = code,
            message = message,
            data = data,
            timestamp = formatTimestamp(),
            meta = mergedMeta,
        )
    }

    private fun mergeMeta(
        meta: Map<String, Any>?,
        data: Any?,
    ): Map<String, Any> {
        val fromContributors =
            contributors
                .flatMap { it.contribute(data).entries }
                .associate { it.toPair() }
        return (meta ?: emptyMap()) + fromContributors
    }

    private fun resolveMessage(responseCode: ResponseCode): Pair<String, String> {
        val metadata: ResponseCodeMetadata? = resolver.resolve(responseCode)

        if (metadata == null) {
            logger.warn(
                "[response] No metadata found for ResponseCode '{}'. Falling back to raw code.",
                responseCode.code,
            )
            return responseCode.code to responseCode.code
        }

        val message =
            messageResolver.resolve(
                metadata.messageKey,
                metadata.defaultMessage ?: metadata.code,
            ) ?: metadata.code

        return metadata.code to message
    }

    private fun formatTimestamp(): String =
        clock
            .instant()
            .atZone(clock.zone)
            .format(props.timestampFormat.formatter())
}
