package com.ecommerce.app.order.viewmodel.checkout

import java.math.BigDecimal

data class CheckoutItemVm(
    val id: Long?,
    val productId: Long?,
    val productName: String?,
    val quantity: Int?,
    val productPrice: BigDecimal?,
    val note: String?,
    val discountAmount: BigDecimal?,
    val taxAmount: BigDecimal?,
    val taxPercent: BigDecimal?,
    val checkoutId: String?
)
