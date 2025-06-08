package io.dopamine.test.support.factory

import io.dopamine.i18n.resolver.MessageResolver
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.factory.DopamineResponseFactory

object DopamineResponseFactoryFixtures {
    fun dummy(
        props: ResponseProperties = ResponseProperties(),
        messageResolver: MessageResolver,
    ): DopamineResponseFactory = DopamineResponseFactory(props, messageResolver)

    fun messageResolverWith(vararg pairs: Pair<String, String>): MessageResolver {
        val map = pairs.toMap()
        return MessageResolver { key, default ->
            key?.let { map[it] } ?: default ?: ""
        }
    }
}
