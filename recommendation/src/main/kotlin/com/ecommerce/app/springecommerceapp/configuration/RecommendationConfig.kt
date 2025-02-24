package com.ecommerce.app.springecommerceapp.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class RecommendationConfig(
    @Value("\${spring-ecommerce-app.services.product}")
    val apiUrl: String,
)
