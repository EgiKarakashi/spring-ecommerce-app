package com.ecommerce.app.customer.model

import jakarta.persistence.*

@Entity
@Table(name = "user_address")
data class UserAddress(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val userId: String? = null,

    val addressId: Long? = null,

    var isActive: Boolean? = null
)
