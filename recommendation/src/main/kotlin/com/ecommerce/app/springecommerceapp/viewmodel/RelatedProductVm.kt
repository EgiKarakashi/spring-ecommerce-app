package com.ecommerce.app.springecommerceapp.viewmodel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class RelatedProductVm(
    @JsonProperty("id")
    val productId: Long,
    val name: String,
    val price: BigDecimal,
    val brand: String,
    val title: String,
    val description: String,
    val metaDescription: String,
    val specification: String,
    val thumbnail: ImageVm,
    val productImages: List<ImageVm>,
    val slug: String
)
