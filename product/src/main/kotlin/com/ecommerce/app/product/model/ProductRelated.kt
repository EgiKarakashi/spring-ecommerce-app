package com.ecommerce.app.product.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "product_related")
data class ProductRelated(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: Product? = null,

    @ManyToOne
    @JoinColumn(name = "related_product_id")
    val relatedProduct: Product? = null
)
