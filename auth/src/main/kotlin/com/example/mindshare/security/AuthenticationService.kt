package com.example.mindshare.security

import com.example.mindshare.domain.auth.LoginRequest
import com.example.mindshare.domain.auth.UserLoginResponse

interface AuthenticationService {
    fun login(loginRequest: LoginRequest): UserLoginResponse
}
