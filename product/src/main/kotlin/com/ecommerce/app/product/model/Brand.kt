package com.ecommerce.app.product.model

import jakarta.persistence.*

@Entity
@Table(name = "brand")
data class Brand(
    @OneToMany(mappedBy = "brand")
    val products: List<Product>? = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String? = null,

    var slug: String? = null,

    var isPublished: Boolean? = null
): AbstractAuditEntity()
