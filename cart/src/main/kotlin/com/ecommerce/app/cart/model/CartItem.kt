package com.ecommerce.app.cart.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table

@Entity
@Table(name = "cart_item")
@IdClass(CartItemId::class)
data class CartItem(
    @Id
    val customerId: String? = null,
    @Id
    val productId: Long? = null,
    var quantity: Int? = null
): AbstractAuditEntity()
