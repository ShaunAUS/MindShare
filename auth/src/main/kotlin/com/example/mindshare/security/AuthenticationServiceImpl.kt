package com.example.mindshare.security

import com.example.mindshare.ErrorCode
import com.example.mindshare.domain.auth.LoginRequest
import com.example.mindshare.domain.auth.UserLoginResponse
import com.example.mindshare.error.exception.ForbiddenException
import com.example.mindshare.jwt.JwtTokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,

) : AuthenticationService {

    override fun login(loginRequest: LoginRequest): UserLoginResponse {
        return createTokens(getAuthentication(loginRequest))
    }

    private fun createTokens(authentication: Authentication): UserLoginResponse {
        return UserLoginResponse(
            accessToken = jwtTokenProvider.createAccessToken(authentication),
            refreshToken = jwtTokenProvider.createRefreshToken(authentication),
        )
    }

    private fun getAuthentication(loginRequest: LoginRequest): Authentication {
        val authentication: Authentication
        try {
            val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.loginId, loginRequest.password)
            authentication = createAuthentication(authenticationToken)
        } catch (e: Exception) {
            throw ForbiddenException(ErrorCode.FORBIDDEN_USER_EXCEPTION)
        }
        return authentication
    }

    private fun createAuthentication(token: UsernamePasswordAuthenticationToken): Authentication {
        return authenticationManagerBuilder.`object`.authenticate(token)
    }
}
