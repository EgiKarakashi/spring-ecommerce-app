package com.ecommerce.app.order.viewmodel.order

import com.ecommerce.app.order.model.OrderItem
import java.math.BigDecimal

data class OrderItemVm(
    val id: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val productPrice: BigDecimal,
    val note: String?,
    val discountAmount: BigDecimal?,
    val taxAmount: BigDecimal?,
    val taxPercent: BigDecimal?,
    val orderId: Long
) {
    companion object {
        fun fromModel(orderItem: OrderItem): OrderItemVm {
            return OrderItemVm(
                id = orderItem.id!!,
                productId = orderItem.productId!!,
                productName = orderItem.productName!!,
                quantity = orderItem.quantity!!,
                productPrice = orderItem.productPrice!!,
                note = orderItem.note,
                discountAmount = orderItem.discountAmount,
                taxPercent = orderItem.taxPercent,
                taxAmount = orderItem.taxAmount,
                orderId = orderItem.orderId!!
            )
        }
    }
}

