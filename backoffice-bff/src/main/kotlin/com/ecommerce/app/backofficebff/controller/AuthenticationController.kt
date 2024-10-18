package com.ecommerce.app.backofficebff.controller

import com.ecommerce.app.backofficebff.viewmodel.AuthenticatedUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController {

    @GetMapping("/authentication/user")
    fun user(@AuthenticationPrincipal principal: OAuth2User): ResponseEntity<AuthenticatedUser> {
        val authenticatedUser = AuthenticatedUser(principal.getAttribute("preferred_username"))
        return ResponseEntity.ok(authenticatedUser)
    }
}
