package io.dopamine.test.support.factory

import io.dopamine.response.core.config.ResponseProperties
import io.dopamine.response.core.factory.DopamineResponseFactory
import io.dopamine.test.support.trace.TestTraceIdResolver

object DopamineResponseFactoryFixtures {
    fun dummy(
        props: ResponseProperties = ResponseProperties(),
        traceId: String = "test-trace-id",
    ): DopamineResponseFactory = DopamineResponseFactory(props, TestTraceIdResolver(traceId))
}
