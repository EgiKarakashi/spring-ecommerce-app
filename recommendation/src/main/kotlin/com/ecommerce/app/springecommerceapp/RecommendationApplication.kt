package com.ecommerce.app.springecommerceapp

import com.ecommerce.app.springecommerceapp.configuration.EmbeddingSearchConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(EmbeddingSearchConfiguration::class)
class RecommendationApplication

fun main(args: Array<String>) {
    runApplication<RecommendationApplication>(*args)
}
