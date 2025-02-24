package com.ecommerce.app.cart

import com.ecommerce.app.commonlibrary.config.CorsConfig
import com.ecommerce.app.commonlibrary.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.ecommerce.app.cart", "com.ecommerce.app.commonlibrary"])
@EnableConfigurationProperties(ServiceUrlConfig::class, CorsConfig::class)
class CartApplication

fun main(args: Array<String>) {
    runApplication<CartApplication>(*args)
}
