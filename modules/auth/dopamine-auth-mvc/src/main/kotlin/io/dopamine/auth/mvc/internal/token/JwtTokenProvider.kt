package io.dopamine.auth.mvc.internal.token

import io.dopamine.auth.common.authentication.Token
import io.dopamine.auth.common.authentication.TokenProvider
import io.dopamine.auth.common.config.AuthProperties
import io.dopamine.auth.common.exception.InvalidTokenException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

/**
 * Default JWT-based implementation of [TokenProvider] using HS256.
 */
class JwtTokenProvider(
    private val properties: AuthProperties,
) : TokenProvider {
    private val key: SecretKey by lazy {
        val secret = properties.token.secret
        require(secret.isNotBlank()) { "JWT secret key must be configured" }
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    override fun generate(payload: Map<String, Any>): Token {
        val now = Instant.now()
        val exp = now.plusSeconds(3600)
        val builder =
            Jwts
                .builder()
                .subject(payload["sub"]?.toString() ?: error("Missing subject"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(payload)
                .signWith(key)

        val jwtString = builder.compact()
        return JwtToken(jwtString, payload["sub"].toString(), now, exp, payload)
    }

    override fun parse(rawToken: String): Token =
        try {
            val parser =
                Jwts
                    .parser()
                    .verifyWith(key)
                    .build()

            val jws = parser.parseSignedClaims(rawToken)
            val claims = jws.getPayload()

            JwtToken(
                value = rawToken,
                subject = claims.subject,
                issuedAt = claims.issuedAt.toInstant(),
                expiresAt = claims.expiration.toInstant(),
                claims = claims,
            )
        } catch (e: Exception) {
            throw InvalidTokenException("Invalid JWT token", e)
        }

    override fun validate(token: Token): Boolean = token.expiresAt.isAfter(Instant.now())
}
