package com.ecommerce.app.product.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring-ecommerce-app.services")
data class ServiceUrlConfig(
    val media: String,
    val rating: String
)
