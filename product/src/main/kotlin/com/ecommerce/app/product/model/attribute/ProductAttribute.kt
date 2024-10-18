package com.ecommerce.app.product.model.attribute

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "product_attribute")
data class ProductAttribute(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String? = null,

    @ManyToOne
    @JoinColumn(name = "product_attribute_group_id")
    val productAttributeGroup: ProductAttributeGroup? = null,

    @OneToMany(mappedBy = "productAttribute")
    @JsonIgnore
    val productAttributeTemplates: List<ProductAttributeTemplate>? = mutableListOf(),

    @OneToMany(mappedBy = "productAttribute")
    val attributeValues: List<ProductAttributeValue>? = mutableListOf()
)
