package com.ecommerce.app.order.viewmodel.order

import com.ecommerce.app.order.viewmodel.orderaddress.OrderAddressVm

data class OrderBriefVm(
    val id: Long,
    val email: String,
    val billingAddressVm: OrderAddressVm
)
