package com.ecommerce.app.product.viewmodel.product

data class ProductEsDetailVm(
    val id: Long?,
    val name: String?,
    val slug: String?,
    val price: Double?,
    val isPublished: Boolean?,
    val isVisibleIndividually: Boolean?,
    val isAllowedToOrder: Boolean?,
    val isFeatured: Boolean?,
    val thumbnailMediaId: Long?,
    val brand: String?,
    val categories: List<String>?,
    val attributes: List<String>?
)
