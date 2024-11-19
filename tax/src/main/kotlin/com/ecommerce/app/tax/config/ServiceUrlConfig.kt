package com.ecommerce.app.tax.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring-ecommerce-app.services")
data class ServiceUrlConfig(val location: String?)
