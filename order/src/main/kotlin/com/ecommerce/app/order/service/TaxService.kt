package com.ecommerce.app.order.service

import com.ecommerce.app.order.config.ServiceUrlConfig
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class TaxService(
    val restClient: RestClient,
    val serviceUrlConfig: ServiceUrlConfig
): AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleDoubleFallback")
    fun getTaxPercentByAddress(taxClassId: Long, countryId: Long, stateOrProvinceId: Long, zipCode: String): Double? {
        val url = UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.tax!!)
            .path("/backoffice/tax-rates/tax-percent")
            .queryParam("taxClassId", taxClassId)
            .queryParam("countryId", countryId)
            .queryParam("stateOrProvinceId", stateOrProvinceId)
            .queryParam("zipCode", zipCode)
            .build().toUri()

        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        return restClient.get()
            .uri(url)
            .headers { h -> h.setBearerAuth(jwt) }
            .retrieve()
            .body(Double::class.java)
    }

    @Throws(Throwable::class)
    fun handleDoubleFallback(throwable: Throwable): Double? {
        return handleTypeFallback(throwable)
    }
}
