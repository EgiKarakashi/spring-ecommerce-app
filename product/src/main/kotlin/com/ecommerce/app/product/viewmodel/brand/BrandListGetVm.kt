package com.ecommerce.app.product.viewmodel.brand

data class BrandListGetVm(
    val brandContent: List<BrandVm>? = null,
    val pageNo: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null,
    val isLast: Boolean? = null
)
