package com.example.mindshare.auth

import com.example.mindshare.domain.auth.LoginRequest
import com.example.mindshare.domain.auth.UserLoginResponse
import com.example.mindshare.security.AuthenticationService
import com.example.mindshare.template.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationService: AuthenticationService,

) {
    @PostMapping("login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Response<UserLoginResponse>> {
        val createTokenInfo = authenticationService.login(loginRequest)
        return ResponseEntity.ok(Response.success(createTokenInfo))
    }
}
