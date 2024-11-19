package com.ecommerce.app.order.viewmodel.orderaddress

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OrderAddressPostVm(
    @field:NotBlank val contactName: String,
    @field:NotBlank val phone: String,
    @field:NotBlank val addressLine1: String,
    val addressLine2: String?,
    @field:NotBlank val city: String,
    @field:NotBlank val zipCode: String,
    @field:NotNull val districtId: Long,
    @field:NotBlank val districtName: String,
    @field:NotNull val stateOrProvinceId: Long,
    @field:NotBlank val stateOrProvinceName: String,
    @field:NotNull val countryId: Long,
    @field:NotBlank val countryName: String
)
