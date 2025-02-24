package com.ecommerce.app.cart.controller

import com.ecommerce.app.cart.service.CartItemService
import com.ecommerce.app.cart.viewmodel.CartItemGetVm
import com.ecommerce.app.cart.viewmodel.CartItemPostVm
import com.ecommerce.app.cart.viewmodel.CartItemPutVm
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CartItemController(
    private val cartItemService: CartItemService
) {

    @PostMapping("/storefront/cart/items")
    fun addCartItem(@Valid @RequestBody cartItemPostVm: CartItemPostVm): ResponseEntity<CartItemGetVm> {
        val cartItemGetVm = cartItemService.addCartItem(cartItemPostVm)
        return ResponseEntity.ok(cartItemGetVm)
    }

    @PutMapping("/storefront/cart/items/{productId}")
    fun updateCartItem(@PathVariable productId: Long, @Valid @RequestBody cartItemPutVm: CartItemPutVm): ResponseEntity<CartItemGetVm> {
        val cartItemGetVm = cartItemService.updateCartItem(productId, cartItemPutVm)
        return ResponseEntity.ok(cartItemGetVm)
    }

    @GetMapping("/storefront/cart/items")
    fun getCartItems(): ResponseEntity<List<CartItemGetVm>> {
        val cartItemGetVms = cartItemService.getCartItems()
        return ResponseEntity.ok(cartItemGetVms)
    }
}
