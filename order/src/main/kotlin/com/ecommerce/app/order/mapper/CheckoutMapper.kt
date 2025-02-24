package com.ecommerce.app.order.mapper

import com.ecommerce.app.order.model.Checkout
import com.ecommerce.app.order.model.CheckoutItem
import com.ecommerce.app.order.viewmodel.checkout.CheckoutItemPostVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutItemVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutPostVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutVm
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.springframework.stereotype.Component
import java.math.BigDecimal

//@Mapper(componentModel = "spring")
//@Component
//interface CheckoutMapper {
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "checkout", ignore = true)
//    fun toModel(checkoutItemPostVm: CheckoutItemPostVm?): CheckoutItem?
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "checkoutState", ignore = true)
//    @Mapping(target = "totalAmount", source = "totalAmount")
//    @Mapping(target = "totalDiscountAmount", source = "totalDiscountAmount")
//    fun toModel(checkoutPostVm: CheckoutPostVm?): Checkout?
//
//    fun toVm(checkoutItem: CheckoutItem?): CheckoutItemVm?
//
//    @Mapping(target = "checkoutItemVms", ignore = true)
//    fun toVm(checkout: Checkout?): CheckoutVm?
//}

@Component
class CheckoutMapper {
    fun toModel(checkoutItemPostVm: CheckoutItemPostVm?): CheckoutItem? {
        if (checkoutItemPostVm == null) return null

        return CheckoutItem(
            id = null,
            checkout = null
        )
    }

    fun toModel(checkoutPostVm: CheckoutPostVm?): Checkout? {
        if (checkoutPostVm == null) return null

        return Checkout(
            id = null,
            checkoutState = null,
            totalAmount = checkoutPostVm.totalAmount,
            totalDiscountAmount = checkoutPostVm.totalDiscountAmount
        )
    }

    fun toVm(checkoutItem: CheckoutItem?): CheckoutItemVm? {
        if (checkoutItem == null) return null

        return CheckoutItemVm(
            id = checkoutItem.id!!,
            productId = null,
            productName = null,
            quantity = null,
            productPrice = null,
            note = null,
            discountAmount = null,
            taxAmount = null,
            taxPercent = null,
            checkoutId = null
        )
    }

    fun toVm(checkout: Checkout?): CheckoutVm? {
        if (checkout == null) return null

        return CheckoutVm(
            id = checkout.id,
            checkoutItemVms = emptyList(),
            email = null,
            note = null,
            couponCode = null,
            totalAmount = null,
            totalDiscountAmount = null
        )
    }
}

