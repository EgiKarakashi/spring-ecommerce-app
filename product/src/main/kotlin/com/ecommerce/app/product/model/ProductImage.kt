package com.ecommerce.app.product.model

import jakarta.persistence.*

@Entity
@Table(name = "product_image")
data class ProductImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val imageId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product? =null
)
