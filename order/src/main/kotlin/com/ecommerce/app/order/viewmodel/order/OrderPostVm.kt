package com.ecommerce.app.order.viewmodel.order

import com.ecommerce.app.order.model.enumeration.DeliveryMethod
import com.ecommerce.app.order.model.enumeration.PaymentMethod
import com.ecommerce.app.order.model.enumeration.PaymentStatus
import com.ecommerce.app.order.viewmodel.orderaddress.OrderAddressPostVm
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class OrderPostVm(
    @field:NotBlank val checkoutId: String,
    @field:NotBlank val email: String,
    @field:NotNull val shippingAddressPostVm: OrderAddressPostVm,
    @field:NotNull val billingAddressPostVm: OrderAddressPostVm,
    val note: String? = null,
    val tax: Float = 0f,
    val discount: Float = 0f,
    val numberItem: Int = 0,
    @field:NotNull val totalPrice: BigDecimal,
    val deliveryFee: BigDecimal? = null,
    val couponCode: String? = null,
    @field:NotNull val deliveryMethod: DeliveryMethod,
    @field:NotNull val paymentMethod: PaymentMethod,
    @field:NotNull val paymentStatus: PaymentStatus,
    @field:NotNull val orderItemPostVms: List<OrderItemPostVm>
)
