package com.ecommerce.app.product.viewmodel.product

data class ProductThumbnailVm(
    val id: Long,
    val name: String,
    val slug: String,
    val thumbnailUrl: String?
)
