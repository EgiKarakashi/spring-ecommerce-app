package com.ecommerce.app.product.model.attribute

import jakarta.persistence.*

@Entity
@Table(name = "product_attribute_group")
data class ProductAttributeGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String? = null
)
