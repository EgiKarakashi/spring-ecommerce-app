package com.ecommerce.app.product.model

import jakarta.persistence.*

@Entity
@Table(name = "product_option")
data class ProductOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String? = null
): AbstractAuditEntity()
