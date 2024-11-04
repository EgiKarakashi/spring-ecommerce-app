package com.ecommerce.app.customer.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(value = "keycloak")
data class KeycloakPropsConfig(
    var authServerUrl: String? = "",
    var realm: String? = "",
    var resource: String? = "",
    var credentials: Credentials? = null
)

object Credentials {
    var secret: String? = null
}
