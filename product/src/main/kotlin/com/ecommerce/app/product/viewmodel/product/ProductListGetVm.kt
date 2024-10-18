package com.ecommerce.app.product.viewmodel.product

data class ProductListGetVm(
    val productContent: List<ProductListVm>,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int,
    val isLast: Boolean
)
