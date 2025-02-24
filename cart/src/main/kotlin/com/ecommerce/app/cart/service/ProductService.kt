package com.ecommerce.app.cart.service

import com.ecommerce.app.cart.viewmodel.ProductThumbnailVm
import com.ecommerce.app.commonlibrary.config.ServiceUrlConfig
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.springframework.core.ParameterizedTypeReference
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class ProductService(
    private val restClient: RestClient,
    private val serviceUrlConfig: ServiceUrlConfig
): AbstractCircuitBreakFallbackHandler() {

    @Retry(name = "restApi")
    @CircuitBreaker(name = "restCircuitBreaker", fallbackMethod = "handleProductThumbnailFallback")
    fun getProducts(ids: List<Long>): List<ProductThumbnailVm>? {
        val jwt = (SecurityContextHolder.getContext().authentication.principal as Jwt).tokenValue
        val url = UriComponentsBuilder
            .fromHttpUrl(serviceUrlConfig.product!!)
            .path("/storefront/products/list-featured")
            .queryParam("productId", ids)
            .build()
            .toUri()

        val typeReference = object : ParameterizedTypeReference<List<ProductThumbnailVm>>() {}

        return restClient.get()
            .uri(url)
            .headers({ it.setBearerAuth(jwt) })
            .retrieve()
            .toEntity(typeReference)
            .body
    }

    fun getProductById(id: Long): ProductThumbnailVm? {
        val products = getProducts(listOf(id))
        if (CollectionUtils.isEmpty(products)) {
            return null
        }
        return products?.first()
    }

    fun existsById(id: Long): Boolean {
        return getProductById(id) != null
    }

    fun handleProductThumbnailFallback(ids: List<Long>, throwable: Throwable): List<ProductThumbnailVm>? {
        return handleTypedFallback(throwable)
    }
}
