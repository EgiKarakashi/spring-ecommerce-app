package com.ecommerce.app.order.viewmodel.checkout

import java.math.BigDecimal

data class CheckoutPostVm(
    val email: String,
    val note: String,
    val couponCode: String,
    val totalAmount: BigDecimal,
    val totalDiscountAmount: BigDecimal,
    val checkoutItemPostVms: List<CheckoutItemPostVm>
)
