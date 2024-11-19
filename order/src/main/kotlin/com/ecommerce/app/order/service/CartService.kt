package com.ecommerce.app.order.service

import com.ecommerce.app.order.config.ServiceUrlConfig
import com.ecommerce.app.order.viewmodel.cart.CartItemDeleteVm
import com.ecommerce.app.order.viewmodel.order.OrderVm
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class CartService(
    val restClient: RestClient,
    val serviceUrlConfig: ServiceUrlConfig
): AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleBodilessFallback")
    fun deleteCartItems(orderVm: OrderVm) {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val cartItemDeleteVms = orderVm.orderItemVms
            .stream()
            .map { orderItemVm -> CartItemDeleteVm(orderItemVm.productId, orderItemVm.quantity) }
            .toList()

        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.cart!!)
            .path("/storefront/cart/items/remove")
            .buildAndExpand()
            .toUri()

        restClient.post()
            .uri(url)
            .headers { h -> h.setBearerAuth(jwt) }
            .body(cartItemDeleteVms)
            .retrieve()
    }
}
