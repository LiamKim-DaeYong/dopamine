package io.dopamine.auth.mvc.config

import io.dopamine.auth.common.authentication.TokenProvider
import io.dopamine.auth.common.authentication.TokenStore
import io.dopamine.auth.common.authentication.UserLookupService
import io.dopamine.auth.common.config.AuthProperties
import io.dopamine.auth.mvc.internal.store.InMemoryTokenStore
import io.dopamine.auth.mvc.internal.token.JwtTokenProvider
import io.dopamine.auth.mvc.internal.user.InMemoryUserLookupService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AuthProperties::class)
class AuthMvcAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun tokenProvider(properties: AuthProperties): TokenProvider = JwtTokenProvider(properties)

    @Bean
    @ConditionalOnMissingBean
    fun userLookupService(): UserLookupService = InMemoryUserLookupService()

    @Bean
    @ConditionalOnMissingBean
    fun tokenStore(): TokenStore = InMemoryTokenStore()
}
