package com.ecommerce.app.product.model.attribute

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "product_attribute")
data class ProductAttribute(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 1L,

    var name: String = "",

    @ManyToOne
    @JoinColumn(name = "product_attribute_group_id")
    var productAttributeGroup: ProductAttributeGroup = ProductAttributeGroup(),

    @OneToMany(mappedBy = "productAttribute")
    @JsonIgnore
    val productAttributeTemplates: List<ProductAttributeTemplate>? = mutableListOf(),

    @OneToMany(mappedBy = "productAttribute")
    val attributeValues: List<ProductAttributeValue>? = mutableListOf()
)
