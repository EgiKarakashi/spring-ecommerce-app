package com.ecommerce.app.rating.service

import com.ecommerce.app.rating.config.ServiceUrlConfig
import com.ecommerce.app.rating.viewmodel.OrderExistsByProductAndUserGetVm
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class OrderService(
    private val restClient: RestClient,
    private val serviceUrlConfig: ServiceUrlConfig
): AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restClientCircuitBreaker", fallbackMethod = "handleFallback")
    fun checkOrderExistsByProductAndUserWithStatus(productId: Long): OrderExistsByProductAndUserGetVm? {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url: URI = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.order)
            .path("/storefront/orders/completed")
            .queryParam("productId", productId.toString())
            .buildAndExpand()
            .toUri()

        return restClient.get()
            .uri(url)
            .headers { h -> h.setBearerAuth(jwt) }
            .retrieve()
            .body(OrderExistsByProductAndUserGetVm::class.java)
    }

    @Throws(Throwable::class)
    override fun <T> handleFallback(throwable: Throwable): T? {
        @Suppress("UNCHECKED_CAST")
        return OrderExistsByProductAndUserGetVm(false) as T
    }

}
