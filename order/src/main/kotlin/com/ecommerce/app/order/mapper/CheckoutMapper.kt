package com.ecommerce.app.order.mapper

import com.ecommerce.app.order.model.Checkout
import com.ecommerce.app.order.model.CheckoutItem
import com.ecommerce.app.order.viewmodel.checkout.CheckoutItemPostVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutItemVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutPostVm
import com.ecommerce.app.order.viewmodel.checkout.CheckoutVm
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Mapper(componentModel = "spring")
@Component
interface CheckoutMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checkout", ignore = true)
    fun toModel(checkoutItemPostVm: CheckoutItemPostVm): CheckoutItem

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "checkoutState", ignore = true)
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "totalDiscountAmount", source = "totalDiscountAmount")
    fun toModel(checkoutPostVm: CheckoutPostVm): Checkout

    fun toVm(checkoutItem: CheckoutItem): CheckoutItemVm

    @Mapping(target = "checkoutItemVms", ignore = true)
    fun toVm(checkout: Checkout): CheckoutVm

    fun map(value: BigDecimal?): BigDecimal = value ?: BigDecimal.ZERO
}
