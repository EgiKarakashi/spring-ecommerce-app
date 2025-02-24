package com.ecommerce.app.order.mapper

import com.ecommerce.app.order.model.csv.OrderItemCsv
import com.ecommerce.app.order.viewmodel.order.OrderBriefVm
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.stereotype.Component

//@Mapper(componentModel = "spring")
//@Component
//interface OrderMapper {
//    @Mapping(target = "phone", source = "billingAddressVm.phone")
//    @Mapping(target = "id", source = "id")
//    fun toCsv(orderItem: OrderBriefVm?): OrderItemCsv?
//}

@Component
class OrderMapper {
    fun toCsv(orderItem: OrderBriefVm?): OrderItemCsv? {
        if (orderItem == null) return null

        return OrderItemCsv(
            id = orderItem.id,
            phone = orderItem.billingAddressVm.phone
        )
    }
}
