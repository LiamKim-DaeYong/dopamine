package io.dopamine.auth.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for Dopamine authentication behavior.
 * Bound to the "dopamine.auth" prefix in application settings.
 */
@ConfigurationProperties(prefix = AuthPropertyKeys.PREFIX)
class AuthProperties {
    var enabled: Boolean = true

    /**
     * Token-related properties.
     */
    var token: Token = Token()

    class Token {
        /**
         * Secret key used to sign JWT tokens (Base64-encoded or raw).
         */
        var secret: String = "dopamine-default-jwt-signing-key-256!"

        /**
         * Token expiration time in seconds.
         */
        var expirationSeconds: Long = 3600
    }
}
