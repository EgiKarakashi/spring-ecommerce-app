package com.ecommerce.app.order.viewmodel.checkout

import com.ecommerce.app.order.model.Checkout
import java.math.BigDecimal

data class CheckoutVm(
    val id: String?,
    val email: String?,
    val note: String?,
    val couponCode: String?,
    val totalAmount: BigDecimal?,
    val totalDiscountAmount: BigDecimal?,
    val checkoutItemVms: List<Checkout>?
)
