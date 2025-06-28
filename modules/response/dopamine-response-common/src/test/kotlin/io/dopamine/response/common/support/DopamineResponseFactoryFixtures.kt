package io.dopamine.response.common.support

import io.dopamine.core.resolver.MessageResolver
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.factory.DopamineResponseFactory
import io.dopamine.response.common.metadata.MetaContributor
import io.dopamine.response.common.metadata.ResponseCodeRegistry
import java.time.Clock

object DopamineResponseFactoryFixtures {
    fun dummy(
        props: ResponseProperties = ResponseProperties(),
        registry: ResponseCodeRegistry = DummyCodeRegistry(),
        messageResolver: MessageResolver = DummyMessageResolver(),
        contributors: List<MetaContributor> = emptyList(),
        clock: Clock = Clock.systemDefaultZone(),
    ): DopamineResponseFactory =
        DopamineResponseFactory(
            props = props,
            registry = registry,
            messageResolver = messageResolver,
            contributors = contributors,
            clock = clock,
        )

    fun messageResolverWith(vararg pairs: Pair<String, String>): MessageResolver {
        val map = pairs.toMap()
        return object : MessageResolver {
            override fun resolve(
                messageKey: String?,
                defaultMessage: String?,
            ): String? = messageKey?.let { map[it] } ?: defaultMessage
        }
    }
}
