package com.example.mindshare.jwt

import com.example.mindshare.ErrorCode
import com.example.mindshare.error.exception.BusinessException
import com.example.mindshare.error.exception.JwtExpiredException
import com.example.mindshare.redis.RedisRepository
import core.enums.JwtCode
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.util.Objects

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisRepository: RedisRepository,
    private val authorizationHeader: String,
    private val refreshHeader: String,

) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val refreshToken = resolveToken(request as HttpServletRequest, refreshHeader)
        val accessToken = resolveToken(request, authorizationHeader)

        if (accessToken != null && refreshToken == null) {
            try {
                checkFromRedis(accessToken)

                val jwtCode = jwtTokenProvider.validateToken(accessToken)

                when (jwtCode) {
                    JwtCode.ACCESS -> setSecurityContextHolder(accessToken)
                    JwtCode.EXPIRED -> throw JwtExpiredException(ErrorCode.EXPIRED_JWT_TOKEN_EXCEPTION)
                    else -> throw JwtException("jwt denied")
                }
            } catch (e: Exception) {
                request.setAttribute("exception", e)
            }
        } else if (accessToken == null && refreshToken != null) {
            request.setAttribute("exception", throw BusinessException(ErrorCode.ONLY_REFRESH_TOKEN_EXCEPTION))
        }

        chain?.doFilter(request, response)
    }

    // acccessToken이 redis에 있으면 로그아웃 유저임
    fun checkFromRedis(accessToken: String) {
        val redisResult = redisRepository.findByToken(accessToken)
        redisResult?.let {
            if (Objects.equals(redisResult, "logout")) {
                throw BusinessException(ErrorCode.LOGOUT_USER_ACCESS_TOKEN_EXCPETION)
            }
        }
    }

    private fun setSecurityContextHolder(accessToken: String) {
        val authentication = jwtTokenProvider.getAuthentication(accessToken)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun resolveToken(request: HttpServletRequest, header: String): String? {
        val bearerToken = request.getHeader(header)

        return if (header == authorizationHeader && bearerToken != null) {
            bearerToken.substring(7)
        } else {
            bearerToken
        }
    }
}
