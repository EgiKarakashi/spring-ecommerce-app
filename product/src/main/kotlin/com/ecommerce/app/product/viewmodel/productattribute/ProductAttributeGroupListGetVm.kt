package com.ecommerce.app.product.viewmodel.productattribute

data class ProductAttributeGroupListGetVm(
    val productAttributeGroupContent: List<ProductAttributeGroupVm>? =null,
    val pageNo: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null,
    val isLast: Boolean? = null
)
