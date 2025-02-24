package com.ecommerce.app.cart.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaRepositories("com.ecommerce.app.cart.repository")
@EntityScan("com.ecommerce.app.cart.model")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
class DatabaseConfig {

    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware {
            val auth: Authentication? = SecurityContextHolder.getContext().authentication
            Optional.of(auth?.name ?: "")
        }
    }
}
