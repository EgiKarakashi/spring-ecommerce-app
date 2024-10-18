package com.ecommerce.app.product

import com.ecommerce.app.product.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ServiceUrlConfig::class)
class ProductApplication

fun main(args: Array<String>) {
	runApplication<ProductApplication>(*args)
}
