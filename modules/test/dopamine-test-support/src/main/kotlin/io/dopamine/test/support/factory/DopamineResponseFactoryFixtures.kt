package io.dopamine.test.support.factory

import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.factory.DopamineResponseFactory

object DopamineResponseFactoryFixtures {
    fun dummy(props: ResponseProperties = ResponseProperties()): DopamineResponseFactory =
        DopamineResponseFactory(props)
}
