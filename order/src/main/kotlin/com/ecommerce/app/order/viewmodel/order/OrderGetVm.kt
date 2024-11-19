package com.ecommerce.app.order.viewmodel.order

import com.ecommerce.app.order.model.Order
import com.ecommerce.app.order.model.OrderItem
import com.ecommerce.app.order.model.enumeration.DeliveryMethod
import com.ecommerce.app.order.model.enumeration.DeliveryStatus
import com.ecommerce.app.order.model.enumeration.OrderStatus
import java.math.BigDecimal
import java.time.ZonedDateTime

data class OrderGetVm(
    val id: Long?,
    val orderStatus: OrderStatus?,
    val totalPrice: BigDecimal?,
    val deliveryStatus: DeliveryStatus?,
    val deliveryMethod: DeliveryMethod?,
    val orderItems: List<OrderItemGetVm>?,
    val createdOn: ZonedDateTime?
) {
    companion object {
        fun fromModel(order: Order, orderItems: Set<OrderItem>?): OrderGetVm {
            return OrderGetVm(
                order.id,
                order.orderStatus,
                order.totalPrice,
                order.deliveryStatus,
                order.deliveryMethod,
                OrderItemGetVm.fromModels(orderItems!!),
                order.createdOn
            )
        }
    }
}
