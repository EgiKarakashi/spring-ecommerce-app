package com.ecommerce.app.product.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String? = null,

    var description: String? = null,

    var slug: String? = null,

    var metaKeyword: String? = null,

    var metaDescription: String? = null,

    var displayOrder: Short? = null,

    var isPublished: Boolean? = null,

    var imageId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "parent_id")
    var parent: Category? = null,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.REMOVE])
    @JsonIgnore
    val categories: List<Category>? = mutableListOf(),

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    val productCategories: List<ProductCategory>? = mutableListOf()
): AbstractAuditEntity() {
    override fun toString(): String {
        return "Category(id=$id, name=$name, description=$description, slug=$slug)"
    }
}
