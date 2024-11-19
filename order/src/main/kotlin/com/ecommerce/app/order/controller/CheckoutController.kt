package com.ecommerce.app.order.controller

import com.ecommerce.app.order.service.CheckoutService
import com.ecommerce.app.order.viewmodel.checkout.CheckoutPostVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutStatusPutVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutVm
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class CheckoutController(
    val checkoutService: CheckoutService
) {

    @PostMapping("/storefront/checkouts")
    fun createCheckout(@Valid @RequestBody checkoutPostVm: CheckoutPostVm): ResponseEntity<CheckoutVm> {
        return ResponseEntity.ok(checkoutService.createCheckout(checkoutPostVm))
    }

    @PutMapping("/storefront/checkouts/status")
    fun updateCheckoutStatus(@Valid @RequestBody checkoutStatusPutVm: CheckoutStatusPutVm): ResponseEntity<Long> {
        return ResponseEntity.ok(checkoutService.updateCheckoutStatus(checkoutStatusPutVm))
    }

    @GetMapping("/storefront/checkouts/{id}")
    fun getOrderWithItemsById(@PathVariable id: String): ResponseEntity<CheckoutVm> {
        return ResponseEntity.ok(checkoutService.getCheckoutPendingStateWithItemsById(id))
    }
}
