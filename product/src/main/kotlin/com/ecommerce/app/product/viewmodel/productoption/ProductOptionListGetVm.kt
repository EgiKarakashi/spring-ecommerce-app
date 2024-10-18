package com.ecommerce.app.product.viewmodel.productoption

data class ProductOptionListGetVm(
    val productOptionContent: List<ProductOptionGetVm>,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Int,
    val totalPages: Int,
    val isLast: Boolean
)
