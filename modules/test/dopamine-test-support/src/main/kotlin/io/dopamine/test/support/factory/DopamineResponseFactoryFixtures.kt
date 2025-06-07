package io.dopamine.test.support.factory

import io.dopamine.i18n.resolver.MessageResolver
import io.dopamine.response.common.config.ResponseProperties
import io.dopamine.response.common.factory.DopamineResponseFactory

object DopamineResponseFactoryFixtures {
    fun dummy(
        props: ResponseProperties = ResponseProperties(),
        messageResolver: MessageResolver = MessageResolver { key, _ -> key },
    ): DopamineResponseFactory = DopamineResponseFactory(props, messageResolver)
}
