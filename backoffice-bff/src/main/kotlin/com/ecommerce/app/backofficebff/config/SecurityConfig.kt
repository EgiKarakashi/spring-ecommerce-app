package com.ecommerce.app.backofficebff.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.security.web.server.SecurityWebFilterChain
import java.util.stream.Collectors

@Configuration
@EnableWebFluxSecurity
class SecurityConfig{

    private val REALM_ACCESS_CLAIM: String = "realm_access"
    private val ROLES_CLAIM: String = "roles"

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange { auth -> auth
                .pathMatchers("/health", "/actuator/prometheus", "/actuator/health").permitAll()
                .anyExchange().hasAnyRole("ADMIN")}
            .oauth2Login(Customizer.withDefaults())
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .build()
    }

    @Bean
    fun userAuthoritiesMapperForKeycloak(): GrantedAuthoritiesMapper {
        return GrantedAuthoritiesMapper { authorities ->
            val mappedAuthorities = mutableSetOf<GrantedAuthority>()
            val authority = authorities.iterator().next()
            val isOidc = authority is OidcUserAuthority

            if (isOidc) {
                val oidcUserAuthority = authority as OidcUserAuthority
                val userInfo = oidcUserAuthority.userInfo

                if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                    val realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM)
                    val roles = realmAccess[ROLES_CLAIM] as Collection<String>
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles))
                }
            } else {
                val oauth2UserAuthority = authority as OAuth2UserAuthority
                val userAttributes = oauth2UserAuthority.attributes

                if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
                    val realmAccess = userAttributes[REALM_ACCESS_CLAIM] as Map<String, Any>
                    val roles = realmAccess[ROLES_CLAIM] as Collection<String>
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles))
                }
            }
            mappedAuthorities
        }
    }

    fun generateAuthoritiesFromClaim(roles: Collection<String>): Collection<GrantedAuthority> {
        return roles.stream()
            .map { role -> SimpleGrantedAuthority("ROLE_$role") }
            .collect(Collectors.toList())
    }
}
