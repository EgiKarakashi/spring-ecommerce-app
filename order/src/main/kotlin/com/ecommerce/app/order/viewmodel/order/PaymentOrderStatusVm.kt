package com.ecommerce.app.order.viewmodel.order

data class PaymentOrderStatusVm(
    val orderId: Long,
    val orderStatus: String,
    val paymentId: Long,
    val paymentStatus: String
)
