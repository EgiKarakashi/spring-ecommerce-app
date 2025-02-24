package com.ecommerce.app.springecommerceapp.viewmodel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductVariationVm(
    val id: Long,
    val name: String,
    val slug: String,
    val sku: String,
    val gtin: String,
    val price: Double,
    val options: Map<Long, String>
)
