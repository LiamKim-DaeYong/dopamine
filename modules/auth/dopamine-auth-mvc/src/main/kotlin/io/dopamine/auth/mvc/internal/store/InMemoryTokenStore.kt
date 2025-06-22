package io.dopamine.auth.mvc.internal.store

import io.dopamine.auth.common.authentication.Token
import io.dopamine.auth.common.authentication.TokenStore
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory implementation of [TokenStore] for testing or simple use cases.
 */
class InMemoryTokenStore : TokenStore {
    private val store = ConcurrentHashMap<String, Token>()

    override fun save(token: Token) {
        store[token.value] = token
    }

    override fun isValid(token: Token): Boolean =
        store[token.value]?.let {
            it.expiresAt.isAfter(token.expiresAt) || it == token
        } ?: true

    override fun invalidate(token: Token) {
        store.remove(token.value)
    }
}
