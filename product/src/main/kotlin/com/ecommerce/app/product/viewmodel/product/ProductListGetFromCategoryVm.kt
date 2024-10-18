package com.ecommerce.app.product.viewmodel.product

data class ProductListGetFromCategoryVm(
    val productContent: List<ProductThumbnailVm>,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int,
    val isLast: Boolean
)
