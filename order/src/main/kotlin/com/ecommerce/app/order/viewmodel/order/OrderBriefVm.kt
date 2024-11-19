package com.ecommerce.app.order.viewmodel.order

import com.ecommerce.app.order.model.Order
import com.ecommerce.app.order.model.enumeration.DeliveryMethod
import com.ecommerce.app.order.model.enumeration.DeliveryStatus
import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.ecommerce.app.order.model.enumeration.PaymentStatus
import com.ecommerce.app.order.viewmodel.orderaddress.OrderAddressVm
import java.math.BigDecimal
import java.time.ZonedDateTime

data class OrderBriefVm(
    val id: Long?,
    val email: String,
    val billingAddressVm: OrderAddressVm,
    val totalPrice: BigDecimal,
    val orderStatus: OrderStatus,
    val deliveryMethod: DeliveryMethod,
    val deliveryStatus: DeliveryStatus,
    val paymentStatus: PaymentStatus,
    val createdOn: ZonedDateTime
) {
    companion object {
        fun fromModel(order: Order): OrderBriefVm {
            return OrderBriefVm(
                id = order.id,
                email = order.email!!,
                billingAddressVm = OrderAddressVm.fromModel(order.billingAddressId!!),
                totalPrice = order.totalPrice!!,
                orderStatus = order.orderStatus!!,
                deliveryMethod = order.deliveryMethod!!,
                deliveryStatus = order.deliveryStatus!!,
                paymentStatus = order.paymentStatus!!,
                createdOn = order.createdOn!!
            )
        }
    }
}
