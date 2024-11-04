package com.ecommerce.app.rating.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring-ecommerce-app.services")
data class ServiceUrlConfig(
    val product: String?,
    val customer: String,
    val order: String
)
