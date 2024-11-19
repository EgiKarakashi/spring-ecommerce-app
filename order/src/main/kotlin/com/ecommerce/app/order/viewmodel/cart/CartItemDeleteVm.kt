package com.ecommerce.app.order.viewmodel.cart

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class CartItemDeleteVm(@NotNull val productId: Long,@NotNull @Min(1) val quantity: Int)
