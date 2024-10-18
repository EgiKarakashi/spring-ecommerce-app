package com.ecommerce.app.product.model

import com.ecommerce.app.product.model.attribute.ProductAttributeValue
import com.ecommerce.app.product.model.enumeration.DimensionUnit
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "product")
data class Product(

    @OneToMany(mappedBy = "product")
    val relatedProducts: List<ProductRelated>? = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String? = null,

    var shortDescription: String? = null,

    var description: String? = null,

    var specification: String? = null,

    var sku: String? = null,

    var gtin: String? = null,

    var slug: String? = null,

    var price: Double? = null,

    var hasOptions: Boolean? = null,

    var isAllowedToOrder: Boolean? = null,

    var isPublished: Boolean? = null,

    var isFeatured: Boolean? = null,

    var isVisibleIndividually: Boolean? = null,

    var stockTrackingEnabled: Boolean? = null,

    var stockQuantity: Long? = null,

    var taxClassId: Long? = null,

    var metaTitle: String? = null,

    var metaKeyword: String? = null,

    var metaDescription: String? = null,

    var thumbnailMediaId: Long? =null,

    var weight: Double? = null,

    @Enumerated(EnumType.STRING)
    var dimensionUnit: DimensionUnit? = null,

    var length: Double? = null,

    var width: Double? = null,

    var height: Double? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonIgnore
    var brand: Brand? = null,

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    val productCategories: List<ProductCategory>? = mutableListOf(),

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    val attributeValues: List<ProductAttributeValue>? = mutableListOf(),

    @OneToMany(mappedBy = "product", cascade = [CascadeType.PERSIST])
    val productImages: List<ProductImage>? = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    val parent: Product? = null,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.REMOVE])
    @JsonManagedReference
    val products: List<Product>? = mutableListOf(),

    val taxIncluded: Boolean? = null

): AbstractAuditEntity() {
    override fun toString(): String {
        return "Product(id=$id, name=$name)"
    }
}
