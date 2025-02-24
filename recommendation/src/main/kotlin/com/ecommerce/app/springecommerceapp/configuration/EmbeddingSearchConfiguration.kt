package com.ecommerce.app.springecommerceapp.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "com.ecommerce.app.springecommerceapp.recommendation.embedding-based.search")
data class EmbeddingSearchConfiguration(
    val similarityThreshold: Double,
    val topK: Int
)
