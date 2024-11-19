package com.ecommerce.app.order.repository

import com.ecommerce.app.order.model.CheckoutItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckoutItemRepository: JpaRepository<CheckoutItem, Long> {

    fun findAllByCheckoutId(checkoutId: String): List<CheckoutItem>
}
