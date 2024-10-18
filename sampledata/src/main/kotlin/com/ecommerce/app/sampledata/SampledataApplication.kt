package com.ecommerce.app.sampledata

import com.ecommerce.app.sampledata.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ServiceUrlConfig::class)
class SampledataApplication

fun main(args: Array<String>) {
    runApplication<SampledataApplication>(*args)
}
