package com.ecommerce.app.customer

import com.ecommerce.app.commonlibrary.config.CorsConfig
import com.ecommerce.app.customer.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ServiceUrlConfig::class, CorsConfig::class)
class CustomerApplication

fun main(args: Array<String>) {
    runApplication<CustomerApplication>(*args)
}
