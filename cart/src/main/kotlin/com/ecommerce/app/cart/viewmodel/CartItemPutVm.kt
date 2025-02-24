package com.ecommerce.app.cart.viewmodel

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class CartItemPutVm(@field:NotNull @field:Min(1) val quantity: Int)
