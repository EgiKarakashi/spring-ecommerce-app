package com.ecommerce.app.rating

import com.ecommerce.app.commonlibrary.config.CorsConfig
import com.ecommerce.app.rating.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.ecommerce.app.rating", "com.ecommerce.app.commonlibrary"])
@EnableConfigurationProperties(ServiceUrlConfig::class, CorsConfig::class)
class RatingApplication

fun main(args: Array<String>) {
    runApplication<RatingApplication>(*args)
}
