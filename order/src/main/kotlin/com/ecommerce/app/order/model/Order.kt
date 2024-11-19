package com.ecommerce.app.order.model

import com.ecommerce.app.order.model.enumeration.DeliveryMethod
import com.ecommerce.app.order.model.enumeration.DeliveryStatus
import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.ecommerce.app.order.model.enumeration.PaymentStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal

@Entity
@Table(name = "`order`")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val email: String? = null,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    val shippingAddressId: OrderAddress? = null,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id")
    val billingAddressId: OrderAddress? = null,
    val note: String? = null,
    @Column(name = "total_tax")
    val tax: Float? = null,
    @Column(name = "total_discount_amount")
    val discount: Float? = null,
    val numberItem: Int? = null,
    @Column(name = "promotion_code")
    val couponCode: String? = null,
    @Column(name = "total_amount")
    val totalPrice: BigDecimal? = null,
    @Column(name = "total_shipment_fee")
    val deliveryFee: BigDecimal? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var orderStatus: OrderStatus? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_method_id")
    val deliveryMethod: DeliveryMethod? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status")
    val deliveryStatus: DeliveryStatus? = null,
    @Enumerated(EnumType.STRING)
    var paymentStatus: PaymentStatus? = null,
    var paymentId: Long? = null,
    val checkoutId: String? = null,
    val rejectReason: String? = null,
    @SuppressWarnings("unused")
    val paymentMethodId: String? = null,
    @SuppressWarnings("unused")
    val  progress: String? = null,
    @SuppressWarnings("unused")
    val  customerId: Long? = null,

    @SuppressWarnings("unused")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "last_error", columnDefinition = "jsonb")
    val  lastError: String? = null,

    @SuppressWarnings("unused")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    val  attributes: String? = null,

    @SuppressWarnings("unused")
    val totalShipmentTax: BigDecimal? = null
): AbstractAuditEntity()
