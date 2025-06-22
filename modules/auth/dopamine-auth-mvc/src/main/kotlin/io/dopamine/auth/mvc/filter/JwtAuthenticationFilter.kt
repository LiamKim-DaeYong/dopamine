package io.dopamine.auth.mvc.filter

import io.dopamine.auth.common.authentication.TokenProvider
import io.dopamine.auth.common.authentication.UserLookupService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT authentication filter that resolves tokens and populates the SecurityContext.
 */
class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val userProvider: UserLookupService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = extractToken(request)

        if (token != null && SecurityContextHolder.getContext().authentication == null) {
            val parsed = tokenProvider.parse(token)

            val principal = userProvider.load(parsed.subject)

            if (principal != null) {
                val auth =
                    UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.roles.map { SimpleGrantedAuthority(it) },
                    ).apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                    }

                SecurityContextHolder.getContext().authentication = auth
            }
        }

        filterChain.doFilter(request, response)
    }

    /**
     * Extracts the token string from the Authorization header.
     */
    private fun extractToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization") ?: return null
        return if (header.startsWith("Bearer ")) header.removePrefix("Bearer ").trim() else null
    }
}
