package com.ecommerce.app.storefrontbff.viewmodel

data class CartItemVm(val productId: Long, val quantity: Int) {
    companion object {
        fun fromCartDetailVm(cartDetailVm: CartDetailVm): CartItemVm {
            return CartItemVm(cartDetailVm.productId, cartDetailVm.quantity)
        }
    }
}
