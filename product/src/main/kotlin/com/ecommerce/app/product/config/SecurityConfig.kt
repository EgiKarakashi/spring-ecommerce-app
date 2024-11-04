package com.ecommerce.app.product.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import kotlin.jvm.Throws

@Configuration
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                auth -> auth
                .requestMatchers("/actuator/prometheus", "/actuator/health/**",
                    "/swagger-ui", "/swagger-ui/**", "/error", "/v3/api-docs/**").permitAll()
                .requestMatchers("/storefront/**").permitAll()
                .requestMatchers("/backoffice/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt() }
            .build()
    }

//    @Bean
//    @Throws(Exception::class)
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        return http
//            .authorizeHttpRequests { auth ->
//                auth
//                    .anyRequest().permitAll() // Allow all requests
//            }
//            .csrf().disable() // Disable CSRF protection if needed
//            .build()
//    }

    @Bean
    fun jwtAuthenticationConverterForKeycloak(): JwtAuthenticationConverter {
        val jwtGrantedAuthoritiesConverter: (Jwt) -> Collection<GrantedAuthority> = { jwt ->
            val realmAccess = jwt.getClaim<Map<String, Collection<String>>>("realm_access")
            val roles = realmAccess["roles"] ?: emptyList()
            roles.map { role -> SimpleGrantedAuthority("ROLE_$role") }.toList()
        }

        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)
        return jwtAuthenticationConverter
    }
}
