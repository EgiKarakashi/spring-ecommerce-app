package com.ecommerce.app.media.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ecommerce")
data class EcommerceConfig(
    val publicUrl: String
)
