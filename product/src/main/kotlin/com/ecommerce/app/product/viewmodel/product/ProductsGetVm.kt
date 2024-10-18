package com.ecommerce.app.product.viewmodel.product

data class ProductsGetVm(
    val productCount: List<ProductThumbnailGetVm>?,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int,
    val isLast: Boolean
)
