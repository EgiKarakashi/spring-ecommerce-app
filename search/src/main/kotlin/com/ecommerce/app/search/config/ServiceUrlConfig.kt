package com.ecommerce.app.search.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring-ecommerce-app.services")
data class ServiceUrlConfig(val product: String?)
