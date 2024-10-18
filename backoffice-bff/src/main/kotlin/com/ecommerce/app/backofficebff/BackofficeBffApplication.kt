package com.ecommerce.app.backofficebff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableWebFluxSecurity
class BackofficeBffApplication

fun main(args: Array<String>) {
    runApplication<BackofficeBffApplication>(*args)
}
