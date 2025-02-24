package com.ecommerce.app.springecommerceapp.service

import com.ecommerce.app.springecommerceapp.configuration.RecommendationConfig
import com.ecommerce.app.springecommerceapp.viewmodel.ProductDetailVm
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import org.springframework.web.util.UriComponentsBuilder

@Service
class ProductService(
    private val restClient: RestClient,
    private val config: RecommendationConfig
) {

    fun getProductDetail(productId: Long): ProductDetailVm? {
        val url = UriComponentsBuilder
            .fromHttpUrl(config.apiUrl)
            .path("/storefront/products/detail/$productId")
            .buildAndExpand()
            .toUri()

        val typeReference = object : ParameterizedTypeReference<ProductDetailVm>() {}

        return restClient.get()
            .uri(url)
            .retrieve()
            .toEntity(typeReference)
            .body
    }
}
