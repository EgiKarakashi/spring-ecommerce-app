package com.ecommerce.app.rating.utils

import com.ecommerce.app.commonlibrary.constants.ApiConstant
import com.ecommerce.app.commonlibrary.exception.AccessDeniedException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

object AuthenticationUtils {
    fun extractUserId(): String {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication is AnonymousAuthenticationToken) {
            throw AccessDeniedException(ApiConstant.ACCESS_DENIED)
        }

        val contextHolder = authentication as JwtAuthenticationToken
        return contextHolder.token.subject
    }
}
