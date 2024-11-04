package com.ecommerce.app.customer.service

import com.ecommerce.app.customer.config.ServiceUrlConfig
import com.ecommerce.app.customer.viewmodel.address.AddressDetailVm
import com.ecommerce.app.customer.viewmodel.address.AddressPostVm
import com.ecommerce.app.customer.viewmodel.address.AddressVm
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class LocationService(
    private val restClient: RestClient,
    private val serviceUrlConfig: ServiceUrlConfig
): AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleAddressDetailListFallback")
    fun getAddressesByIdList(ids: List<Long>): List<AddressDetailVm>? {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.location!!)
            .path("/storefront/addresses")
            .queryParam("ids", ids)
            .buildAndExpand()
            .toUri()

        return restClient.get()
            .uri(url)
            .headers { it.setBearerAuth(jwt) }
            .retrieve()
            .body(object : ParameterizedTypeReference<List<AddressDetailVm>>() {})
    }

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleAddressDetailFallback")
    fun getAddressById(id: Long): AddressDetailVm? {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.location!!)
            .path("/storefront/addresses/{id}")
            .buildAndExpand(id)
            .toUri()

        return restClient.get()
            .uri(url)
            .headers { it.setBearerAuth(jwt) }
            .retrieve()
            .body(AddressDetailVm::class.java)
    }

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleAddressFallback")
    fun createAddress(addressPostVm: AddressPostVm): AddressVm? {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.location!!)
            .path("/storefront/addresses")
            .buildAndExpand()
            .toUri()

        return restClient.post()
            .uri(url)
            .headers { it.setBearerAuth(jwt) }
            .body(addressPostVm)
            .retrieve()
            .body(AddressVm::class.java)
    }

    @Throws(Throwable::class)
    fun handleAddressDetailListFallback(throwable: Throwable): List<AddressDetailVm>? {
        return handleTypeFallback(throwable)
    }

    @Throws(Throwable::class)
    fun handleAddressDetailFallback(throwable: Throwable): AddressDetailVm? {
        return handleTypeFallback(throwable)
    }

    @Throws(Throwable::class)
    fun handleAddressFallback(throwable: Throwable): AddressVm? {
        return handleTypeFallback(throwable)
    }
}
