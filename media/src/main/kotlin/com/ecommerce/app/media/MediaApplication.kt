package com.ecommerce.app.media


import com.ecommerce.app.commonlibrary.config.CorsConfig
import com.ecommerce.app.media.config.EcommerceConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.ecommerce.app.media", "com.ecommerce.app.commonlibrary"])
@EnableConfigurationProperties(EcommerceConfig::class, CorsConfig::class)
class MediaApplication

fun main(args: Array<String>) {
    runApplication<MediaApplication>(*args)
}
