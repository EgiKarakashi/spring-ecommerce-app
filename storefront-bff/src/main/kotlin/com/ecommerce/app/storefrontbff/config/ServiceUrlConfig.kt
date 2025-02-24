package com.ecommerce.app.storefrontbff.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring-ecommerce-app.services")
data class ServiceUrlConfig(
    val services: Map<String, String>
)
