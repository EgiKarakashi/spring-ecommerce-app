package com.ecommerce.app.product.model.attribute

import com.ecommerce.app.product.model.Product
import jakarta.persistence.*

@Entity
@Table(name = "product_attribute_value")
data class ProductAttributeValue(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product? = null,

    @ManyToOne
    @JoinColumn(name = "product_attribute_id", nullable = false)
    val productAttribute: ProductAttribute? = null,

    val value: String? = null
)
