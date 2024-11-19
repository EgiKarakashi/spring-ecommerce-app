package com.ecommerce.app.order.viewmodel.order

data class OrderListVm(
    val orderList: List<OrderBriefVm>?,
    val totalElements: Long,
    val totalPages: Int
)
