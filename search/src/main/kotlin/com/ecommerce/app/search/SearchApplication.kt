package com.ecommerce.app.search

import com.ecommerce.app.search.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication(scanBasePackages = ["com.ecommerce.app.search", "com.ecommerce.app.commonlibrary"])
@EnableConfigurationProperties(ServiceUrlConfig::class,)
@Configuration
class SearchApplication

fun main(args: Array<String>) {
    runApplication<SearchApplication>(*args)
}
