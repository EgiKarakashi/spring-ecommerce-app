package com.ecommerce.app.order.viewmodel.orderaddress

import com.ecommerce.app.order.model.OrderAddress

data class OrderAddressVm(
    val id: Long,
    val contactName: String,
    val phone: String,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val zipCode: String,
    val districtId: Long,
    val districtName: String,
    val stateOrProvinceId: Long,
    val stateOrProvinceName: String,
    val countryId: Long,
    val countryName: String
) {
    companion object {
        fun fromModel(orderAddress: OrderAddress): OrderAddressVm {
            return OrderAddressVm(
                id = orderAddress.id,
                phone = orderAddress.phone,
                contactName = orderAddress.contactName,
                addressLine1 = orderAddress.addressLine1,
                addressLine2 = orderAddress.addressLine2,
                city = orderAddress.city,
                zipCode = orderAddress.zipCode,
                districtId = orderAddress.districtId,
                districtName = orderAddress.districtName,
                stateOrProvinceId = orderAddress.stateOrProvinceId,
                stateOrProvinceName = orderAddress.stateOrProvinceName,
                countryId = orderAddress.countryId,
                countryName = orderAddress.countryName
            )
        }
    }
}
