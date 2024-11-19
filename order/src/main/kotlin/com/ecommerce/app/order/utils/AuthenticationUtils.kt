package com.ecommerce.app.order.utils

import com.ecommerce.app.commonlibrary.exception.SignInRequiredException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

object AuthenticationUtils {
    fun getCurrentUserId(): String {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication is AnonymousAuthenticationToken) {
            throw SignInRequiredException(Constants.ErrorCode.SIGN_IN_REQUIRED)
        }

        val contextHolder = authentication as JwtAuthenticationToken
        return contextHolder.token.subject
    }
}

