package com.ecommerce.app.order.model

import com.ecommerce.app.order.model.enumeration.CheckoutState
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal

@Entity
@Table(name = "checkout")
data class Checkout(
    @Id
    val id: String? = null,
    val email: String? = null,
    val note: String? = null,
    @Column(name = "promotion_code")
    val couponCode: String? = null,
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val checkoutState: CheckoutState? = null,
    @SuppressWarnings("unused")
    val progress: String? = null,
    @SuppressWarnings("unused")
    val customerId: Long? = null,
    @SuppressWarnings("unused")
    val shipmentMethodId: String? = null,
    @SuppressWarnings("unused")
    val paymentMethodId: String? = null,
    @SuppressWarnings("unused")
    val shippingAddressId: String? = null,
    @SuppressWarnings("unused")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "last_error", columnDefinition = "jsonb")
    val lastError: String? = null,
    @SuppressWarnings("unused")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    val attributes: String? = null,
    @SuppressWarnings("unused")
    val totalAmount: BigDecimal? = null,
    @SuppressWarnings("unused")
    val totalShipmentFee: BigDecimal? = null,
    @SuppressWarnings("unused")
    val totalShipmentTax: BigDecimal? = null,
    @SuppressWarnings("unused")
    val totalTax: BigDecimal? = null,
    @SuppressWarnings("unused")
    val totalDiscountAmount: BigDecimal? = null
): AbstractAuditEntity()
