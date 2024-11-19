package com.ecommerce.app.order

import com.ecommerce.app.order.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(ServiceUrlConfig::class)
class OrderApplication

fun main(args: Array<String>) {
    runApplication<OrderApplication>(*args)
}
