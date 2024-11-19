package com.ecommerce.app.product.model

import jakarta.persistence.*

@Entity
@Table(name = "product_category")
data class ProductCategory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product? = null,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category? = null,

    val displayOrder: Int? = null,

    val isFeaturedProduct: Boolean? = null
)
