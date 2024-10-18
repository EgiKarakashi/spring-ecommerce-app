package com.ecommerce.app.product.model.attribute

import jakarta.persistence.*

@Entity
@Table(name = "product_attribute_template")
data class ProductAttributeTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_attribute_id", nullable = false)
    val productAttribute: ProductAttribute? = null,

    @ManyToOne
    @JoinColumn(name = "product_template_id", nullable = false)
    val productTemplate: ProductTemplate? = null,

    val displayOrder: Int? = null
)
