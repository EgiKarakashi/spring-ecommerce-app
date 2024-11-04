package com.ecommerce.app.commonlibrary.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring-ecommerce-app.services")
data class ServiceUrlConfig(val media: String?, val product: String?)
