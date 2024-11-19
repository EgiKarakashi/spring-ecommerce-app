package com.ecommerce.app.order.viewmodel.order

import com.ecommerce.app.order.model.OrderItem
import org.springframework.util.CollectionUtils
import java.math.BigDecimal
import java.util.*

data class OrderItemGetVm(
    val id: Long?,
    val productId: Long?,
    val productName: String?,
    val quantity: Int?,
    val productPrice: BigDecimal?,
    val discountAmount: BigDecimal?,
    val taxAmount: BigDecimal?
) {
    companion object {
        fun fromModel(orderItem: OrderItem): OrderItemGetVm {
            return OrderItemGetVm(
                orderItem.id,
                orderItem.productId,
                orderItem.productName,
                orderItem.quantity,
                orderItem.productPrice,
                orderItem.discountAmount,
                orderItem.taxAmount
            )
        }

        fun fromModels(orderItems: Collection<OrderItem>): List<OrderItemGetVm> {
            if (CollectionUtils.isEmpty(orderItems)) {
                return Collections.emptyList()
            }
            return orderItems.stream().map { OrderItemGetVm.fromModel(it) }.toList()
        }
    }
}
