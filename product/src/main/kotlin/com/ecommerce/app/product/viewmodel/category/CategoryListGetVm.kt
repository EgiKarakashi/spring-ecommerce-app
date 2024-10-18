package com.ecommerce.app.product.viewmodel.category

data class CategoryListGetVm(
    val categoryContent: List<CategoryGetVm>? = mutableListOf(),
    val pageNo: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null,
    val isLast: Boolean? = null
)
