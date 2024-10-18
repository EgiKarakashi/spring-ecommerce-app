package com.ecommerce.app.product.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String? = null,

    val description: String? = null,

    val slug: String? = null,

    val metaKeyword: String? = null,

    val metaDescription: String? = null,

    val displayOrder: Short? = null,

    val isPublished: Boolean? = null,

    val imageId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "parent_id")
    val parent: Category? = null,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.REMOVE])
    @JsonIgnore
    val categories: List<Category>? = mutableListOf(),

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    val productCategories: List<ProductCategory>? = mutableListOf()
): AbstractAuditEntity()
