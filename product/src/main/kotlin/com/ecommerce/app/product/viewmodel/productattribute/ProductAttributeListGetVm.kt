package com.ecommerce.app.product.viewmodel.productattribute

data class ProductAttributeListGetVm(
    val productAttributeContent: List<ProductAttributeGetVm>? = mutableListOf(),
    val pageNo: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null,
    val isLast: Boolean? = null
)
