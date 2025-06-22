package io.dopamine.auth.mvc.internal.token

import io.dopamine.auth.common.authentication.Token
import java.time.Instant

data class JwtToken(
    override val value: String,
    override val subject: String,
    override val issuedAt: Instant,
    override val expiresAt: Instant,
    override val claims: Map<String, Any>,
) : Token
