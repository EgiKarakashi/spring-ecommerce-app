package com.ecommerce.app.customer.config

import org.keycloak.OAuth2Constants.*
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakClientConfig(
    private val keycloakPropsConfig: KeycloakPropsConfig
) {

    @Bean
    fun keycloak(): Keycloak {
        return KeycloakBuilder.builder()
            .grantType(CLIENT_CREDENTIALS)
            .serverUrl(keycloakPropsConfig.authServerUrl)
            .realm(keycloakPropsConfig.realm)
            .clientId(keycloakPropsConfig.resource)
            .clientSecret(keycloakPropsConfig.credentials?.secret)
            .build()
    }
}
