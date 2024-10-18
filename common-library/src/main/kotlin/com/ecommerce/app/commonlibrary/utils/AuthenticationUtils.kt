package com.ecommerce.app.commonlibrary.utils

import com.ecommerce.app.commonlibrary.constants.ApiConstant
import com.ecommerce.app.commonlibrary.exception.AccessDeniedException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

object AuthenticationUtils {
        fun extractUserId(): String {
            val authentication = getAuthentication()

            if (authentication is AnonymousAuthenticationToken) {
                throw AccessDeniedException(ApiConstant.ACCESS_DENIED)
            }

            val contextHolder = authentication as JwtAuthenticationToken
            return contextHolder.token.subject
        }

        private fun extractJwt(): String {
            return (getAuthentication().principal as Jwt).tokenValue
        }

        private fun getAuthentication(): Authentication {
            return SecurityContextHolder.getContext().authentication
        }
}
