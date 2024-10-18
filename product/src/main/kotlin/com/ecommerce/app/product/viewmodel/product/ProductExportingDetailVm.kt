package com.ecommerce.app.product.viewmodel.product

data class ProductExportingDetailVm(
    val id: Long?,
    val name: String?,
    val shortDescription: String?,
    val description: String?,
    val specification: String?,
    val sku: String?,
    val gtin: String?,
    val slug: String?,
    val isAllowedToOrder: Boolean?,
    val isPublished: Boolean?,
    val isFeatured: Boolean?,
    val isVisible: Boolean?,
    val stockTrackingEnabled: Boolean?,
    val price: Double?,
    val brandId: Long?,
    val brandName: String?,
    val metaTitle: String?,
    val metaKeyword: String?,
    val metaDescription: String?
)
