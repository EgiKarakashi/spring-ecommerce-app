package com.ecommerce.app.product.model

import jakarta.persistence.*

@Entity
@Table(name = "product_option_value")
data class ProductOptionValue(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product? = null,

    @ManyToOne
    @JoinColumn(name = "product_option_id", nullable = false)
    val productOption: ProductOption? = null,

    val displayType: String? = null,

    val displayOrder: Int? = null,

    val value: String? = null
)
