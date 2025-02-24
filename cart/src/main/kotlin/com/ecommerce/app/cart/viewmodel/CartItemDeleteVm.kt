package com.ecommerce.app.cart.viewmodel

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class CartItemDeleteVm(
   @field:NotNull val productId: Long,
    @field:NotNull @field:Min(1) val quantity: Int
)
