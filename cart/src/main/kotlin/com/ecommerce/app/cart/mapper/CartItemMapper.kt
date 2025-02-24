package com.ecommerce.app.cart.mapper

import com.ecommerce.app.cart.model.CartItem
import com.ecommerce.app.cart.viewmodel.CartItemGetVm
import com.ecommerce.app.cart.viewmodel.CartItemPostVm
import org.springframework.stereotype.Component

@Component
class CartItemMapper {

    fun toGetVm(cartItem: CartItem?): CartItemGetVm {
        return CartItemGetVm(
            customerId = cartItem?.customerId,
            productId = cartItem?.productId,
            quantity = cartItem?.quantity
        )
    }

    fun toCartItem(cartItemPostVm: CartItemPostVm, currentUserId: String): CartItem {
        return CartItem(
            customerId = currentUserId,
            productId = cartItemPostVm.productId,
            quantity = cartItemPostVm.quantity
        )
    }

    fun toCartItem(currentUserId: String, productId: Long, quantity: Int): CartItem {
        return CartItem(
            customerId = currentUserId,
            productId = productId,
            quantity = quantity
        )
    }

    fun toGetVms(cartItems: List<CartItem>): List<CartItemGetVm> {
        return cartItems.map { toGetVm(it) }
    }
}
