package com.ecommerce.app.customer.viewmodel.address

data class AddressDetailVm(
    val id: Long,
    val contactName: String,
    val phone: String,
    val addressLine1: String,
    val city: String,
    val zipCode: String,
    val districtId: Long,
    val districtName: String,
    val stateOrProvinceId: Long,
    val stateOrProvinceName: String,
    val countryId: Long,
    val countryName: String
)
