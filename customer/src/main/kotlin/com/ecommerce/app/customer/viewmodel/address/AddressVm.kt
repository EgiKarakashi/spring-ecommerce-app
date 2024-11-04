package com.ecommerce.app.customer.viewmodel.address

data class AddressVm(
    val id: Long,
    val contactName: String,
    val phone: String,
    val addressLine1: String,
    val city: String,
    val zipCode: String,
    val districtId: Long,
    val stateOrProvinceId: Long,
    val countryId: Long
)
