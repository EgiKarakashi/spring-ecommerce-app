package com.ecommerce.app.order.service

import com.ecommerce.app.order.config.ServiceUrlConfig
import com.ecommerce.app.order.viewmodel.customer.CustomerVm
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import kotlin.jvm.Throws

@Service
class CustomerService (
    private val restClient: RestClient,
    private val serviceUrlConfig: ServiceUrlConfig
): AbstractCircuitBreakFallbackHandler() {


    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleCustomerFallback")
    fun getCustomer(): CustomerVm? {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.customer!!)
            .path("/storefront/customer/profile")
            .buildAndExpand()
            .toUri()

        return restClient.get()
            .uri(url)
            .headers { h -> h.setBearerAuth(jwt) }
            .retrieve()
            .body(CustomerVm::class.java)
    }

    @Throws(Throwable::class)
    fun handleCustomerFallback(throwable: Throwable): CustomerVm? {
        return handleTypeFallback(throwable)
    }
}
