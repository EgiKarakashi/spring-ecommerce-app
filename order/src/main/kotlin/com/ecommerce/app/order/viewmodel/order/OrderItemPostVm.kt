package com.ecommerce.app.order.viewmodel.order

import java.math.BigDecimal

data class OrderItemPostVm(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val productPrice: BigDecimal,
    val note: String,
    val discountAmount: BigDecimal,
    val taxAmount: BigDecimal,
    val taxPercent: BigDecimal
)
