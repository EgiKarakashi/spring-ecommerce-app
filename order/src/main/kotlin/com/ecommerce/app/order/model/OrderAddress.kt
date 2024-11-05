package com.ecommerce.app.order.model

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

data class OrderAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
)
