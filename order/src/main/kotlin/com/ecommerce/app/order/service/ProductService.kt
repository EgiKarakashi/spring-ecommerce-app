package com.ecommerce.app.order.service

import com.ecommerce.app.order.config.ServiceUrlConfig
import com.ecommerce.app.order.viewmodel.order.OrderItemVm
import com.ecommerce.app.order.viewmodel.order.OrderVm
import com.ecommerce.app.order.viewmodel.product.ProductQuantityItem
import com.ecommerce.app.order.viewmodel.product.ProductVariationVm
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class ProductService(
    private val restClient: RestClient,
    private val serviceUrlConfig: ServiceUrlConfig
) : AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleProductVariationListFallback")
    fun getProductVariations(productId: Long): List<ProductVariationVm>? {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.product!!)
            .path("/backoffice/product-variations/$productId")
            .buildAndExpand()
            .toUri()

        val typeReference = object : ParameterizedTypeReference<List<ProductVariationVm>>() {}

        return restClient.get()
            .uri(url)
            .headers { it.setBearerAuth(jwt) }
            .retrieve()
            .toEntity(typeReference)
            .body
    }

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleBodilessFallback")
    fun subtractProductStockQuantity(orderVm: OrderVm) {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.product!!)
            .path("/backoffice/products/subtract-quantity")
            .buildAndExpand()
            .toUri()

        restClient.put()
            .uri(url)
            .headers { it.setBearerAuth(jwt) }
            .body(buildProductQuantityItems(orderVm.orderItemVms))
            .retrieve()
    }

    private fun buildProductQuantityItems(orderItems: Set<OrderItemVm>): List<ProductQuantityItem> {
        return orderItems.map { orderItem ->
            ProductQuantityItem(
                productId = orderItem.productId,
                quantity = orderItem.quantity.toLong()
            )
        }
    }

    protected fun handleProductVariationListFallback(throwable: Throwable): List<ProductVariationVm>? {
        return handleTypeFallback(throwable)
    }
}
