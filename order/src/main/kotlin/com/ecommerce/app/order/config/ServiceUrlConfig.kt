package com.ecommerce.app.order.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring-ecommerce-app.services")
data class ServiceUrlConfig(val cart: String?, val customer: String?, val tax: String?, val product: String?, val promotion: String?)
