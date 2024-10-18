package com.ecommerce.app.product.viewmodel.producttemplate

data class ProductTemplateListGetVm(
    val productTemplateGetVms: List<ProductTemplateGetVm>? = mutableListOf(),
    val pageNo: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null,
    val isLast: Boolean? = null
)
