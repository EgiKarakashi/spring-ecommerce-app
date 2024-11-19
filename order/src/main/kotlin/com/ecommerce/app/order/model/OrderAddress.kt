package com.ecommerce.app.order.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "order_address")
data class OrderAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val contactName: String? = null,
    val phone: String? = null,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val districtId: Long? = null,
    val districtName: String? = null,
    val stateOrProvinceId: Long? = null,
    val stateOrProvinceName: String? = null,
    val countryId: Long? = null,
    val countryName: String? = null
)
