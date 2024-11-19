package com.ecommerce.app.order.viewmodel.order

import com.ecommerce.app.order.model.Order
import com.ecommerce.app.order.model.OrderItem
import com.ecommerce.app.order.model.enumeration.DeliveryMethod
import com.ecommerce.app.order.model.enumeration.DeliveryStatus
import com.ecommerce.app.order.model.enumeration.OrderStatus
import com.ecommerce.app.order.model.enumeration.PaymentStatus
import com.ecommerce.app.order.viewmodel.orderaddress.OrderAddressVm
import java.math.BigDecimal

data class OrderVm(
    val id: Long?,
    val email: String,
    val shippingAddressVm: OrderAddressVm,
    val billingAddressVm: OrderAddressVm,
    val note: String?,
    val tax: Float,
    val discount: Float,
    val numberItem: Int,
    val totalPrice: BigDecimal,
    val deliveryFee: BigDecimal,
    val couponCode: String?,
    val orderStatus: OrderStatus,
    val deliveryMethod: DeliveryMethod,
    val deliveryStatus: DeliveryStatus,
    val paymentStatus: PaymentStatus,
    val orderItemVms: Set<OrderItemVm>,
    val checkoutId: String?
) {
    companion object {
        fun fromModel(order: Order, orderItems: Set<OrderItem>?): OrderVm {
            val orderItemVms = orderItems?.map { OrderItemVm.fromModel(it) }?.toSet() ?: emptySet()

            return OrderVm(
                id = order.id!!,
                email = order.email!!,
                shippingAddressVm = OrderAddressVm.fromModel(order.shippingAddressId!!),
                billingAddressVm = OrderAddressVm.fromModel(order.billingAddressId!!),
                note = order.note,
                tax = order.tax!!,
                discount = order.discount!!,
                numberItem = order.numberItem!!,
                totalPrice = order.totalPrice!!,
                deliveryFee = order.deliveryFee!!,
                couponCode = order.couponCode,
                orderStatus = order.orderStatus!!,
                deliveryMethod = order.deliveryMethod!!,
                deliveryStatus = order.deliveryStatus!!,
                paymentStatus = order.paymentStatus!!,
                orderItemVms = orderItemVms,
                checkoutId = order.checkoutId
            )
        }
    }
}

