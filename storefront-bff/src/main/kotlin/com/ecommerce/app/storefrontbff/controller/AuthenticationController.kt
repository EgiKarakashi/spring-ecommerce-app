package com.ecommerce.app.storefrontbff.controller

import com.ecommerce.app.storefrontbff.viewmodel.AuthenticatedUserVm
import com.ecommerce.app.storefrontbff.viewmodel.AuthenticationInfoVm
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController {

    @GetMapping("/authentication")
    fun user(@AuthenticationPrincipal principal: OAuth2User?): ResponseEntity<AuthenticationInfoVm> {
        if (principal == null) {
            return ResponseEntity.ok(AuthenticationInfoVm(false, null))
        }
        val authenticatedUser = AuthenticatedUserVm(
            principal.getAttribute("preferred_username")
        )
        return ResponseEntity.ok(AuthenticationInfoVm(true, authenticatedUser))
    }
}
