package com.ecommerce.app.search.viewmodel

data class ProductListGetVm(
    val products: List<ProductGetVm>,
    val pageNo: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isLast: Boolean,
    val aggregations: Map<String, Map<String, Long>>
)
