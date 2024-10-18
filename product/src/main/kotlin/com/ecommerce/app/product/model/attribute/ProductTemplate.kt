package com.ecommerce.app.product.model.attribute

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
@Table(name = "product_template")
data class ProductTemplate(
    @Id
    val id: Long? = null,

    val name: String? = null,

    @OneToMany(mappedBy = "productTemplate", cascade = [CascadeType.PERSIST])
    val productAttributeTemplates: List<ProductAttributeTemplate>? = mutableListOf()
)
