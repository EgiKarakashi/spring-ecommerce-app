package com.ecommerce.app.order.model

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal

@Entity
@Table(name = "order_item")
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val productId: Long? = null,
    @Column(name = "order_id")
    val orderId: Long? = null,
    @Column(name = "name")
    val productName: String? = null,
    val quantity: Int? = null,
    @Column(name = "price")
    val productPrice: BigDecimal? = null,
    @Column(name = "description")
    val note: String? = null,
    val discountAmount: BigDecimal? = null,
    val taxAmount: BigDecimal? = null,
    val taxPercent: BigDecimal? = null,
    @SuppressWarnings("unused")
    val shipmentFee:  BigDecimal? = null,
    @SuppressWarnings("unused")
    val status: String? = null,
    @SuppressWarnings("unused")
    val shipmentTax: BigDecimal? = null,
    @SuppressWarnings("unused")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "processing_state", columnDefinition = "jsonb")
    val processingState: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    val order: Order? = null
): AbstractAuditEntity()
