package com.ecommerce.app.tax

import com.ecommerce.app.commonlibrary.config.CorsConfig
import com.ecommerce.app.tax.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.ecommerce.app.tax", "com.ecommerce.app.commonlibrary"])
@EnableConfigurationProperties(ServiceUrlConfig::class, CorsConfig::class)
class TaxApplication

fun main(args: Array<String>) {
    runApplication<TaxApplication>(*args)
}
