package com.ecommerce.app.springecommerceapp.viewmodel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ProductDetailVm(
    val id: Long,
    val name: String,
    val shortDescription: String,
    val description: String,
    val specification: String,
    val sku: String,
    val gtin: String,
    val slug: String,
    val isAllowedToOrder: Boolean,
    val isPublished: Boolean,
    val isFeatured: Boolean,
    val stockTrackingEnabled: Boolean,
    val price: Double,
    val brandId: Long,
    val categories: List<CategoryVm>,
    val metaTitle: String,
    val metaKeyword: String,
    val metaDescription: String,
    val taxClassId: Long,
    val brandName: String,
    val attributeValues: List<ProductAttributeValueVm>,
    val variations: List<ProductVariationVm>,
    val thumbnail: ImageVm,
    val productImages: List<ImageVm>
)
