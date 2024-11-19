package com.ecommerce.app.order.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "checkout_item")
data class CheckoutItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val productId: String? = null,
    @Column(name = "checkout_id")
    var checkoutId: String? = null,
    @Column(name = "name")
    val productName: String? = null,
    val quantity: Int? = null,
    @Column(name = "price")
    val productPrice: BigDecimal? = null,
    @Column(name = "description")
    val note: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id", insertable = false, updatable = false)
    val checkout: Checkout? = null,
    val discountAmount: BigDecimal? = null,
    @Column(name = "tax")
    val taxAmount: BigDecimal? = null,
    val taxPercent: BigDecimal? = null,
    @SuppressWarnings("unused")
    val shipmentTax: BigDecimal? = null,
    @SuppressWarnings("unused")
    val shipmentFee: BigDecimal? = null
): AbstractAuditEntity()
