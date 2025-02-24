package com.ecommerce.app.storefrontbff

import com.ecommerce.app.storefrontbff.config.ServiceUrlConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.web.server.WebFilter

@SpringBootApplication
@EnableWebFluxSecurity
@EnableConfigurationProperties(ServiceUrlConfig::class)
class StorefrontBffApplication {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun writeableHeaders(): WebFilter {
        return WebFilter { exchange, chain ->
            val writeableHeaders = HttpHeaders.writableHttpHeaders(exchange.request.headers)
            val writeableRequest = object : ServerHttpRequestDecorator(exchange.request) {
                override fun getHeaders(): HttpHeaders {
                    return writeableHeaders
                }
            }
            val writeableExchange = exchange.mutate()
                .request(writeableRequest)
                .build()
            chain.filter(writeableExchange)
        }
    }
}
fun main(args: Array<String>) {
    runApplication<StorefrontBffApplication>(*args)
}


