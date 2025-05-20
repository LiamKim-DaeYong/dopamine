package io.dopamine.starter.api

import io.dopamine.docs.config.DocsAutoConfiguration
import io.dopamine.response.config.ResponseAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(
    ResponseAutoConfiguration::class,
    DocsAutoConfiguration::class,
)
class ApiAutoConfiguration
