package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.enumeration.DimensionUnit
import com.ecommerce.app.product.validation.ValidateProductPrice
import com.ecommerce.app.product.viewmodel.productoption.ProductOptionValuePostVm
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import jakarta.validation.constraints.NotBlank


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize
data class ProductPostVm(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val slug: String,
    val brandId: Long = 0L,
    val categoryIds: List<Long> = emptyList(),
    val shortDescription: String = "",
    val description: String = "",
    val specification: String = "",
    val sku: String = "",
    val gtin: String = "",
    val weight: Double = 0.0,
    val dimensionUnit: DimensionUnit,
    val length: Double = 0.0,
    val width: Double = 0.0,
    val height: Double = 0.0,
    @ValidateProductPrice
    val price: Double = 0.0,
    val isAllowedToOrder: Boolean = false,
    val isPublished: Boolean = false,
    val isFeatured: Boolean = false,
    val isVisibleIndividually: Boolean = false,
    val stockTrackingEnabled: Boolean = false,
    val metaTitle: String = "",
    val metaKeyword: String = "",
    val metaDescription: String = "",
    val thumbnailMediaId: Long = 0L,
    val productImageIds: List<Long> = emptyList(),
    val variations: List<ProductVariationPostVm> = emptyList(),
    val productOptionValues: List<ProductOptionValuePostVm> = emptyList(),
    val relatedProductIds: List<Long> = emptyList(),
    val taxClassId: Long = 0L
) : ProductSaveVm<ProductVariationPostVm> {
    override fun variations(): List<ProductVariationPostVm> {
        return variations
    }

    override fun isProductPublished(): Boolean = isPublished
    override fun name(): String {
        return name
    }

    override fun slug(): String {
        return slug
    }

    override fun sku(): String {
        return sku
    }

    override fun gtin(): String {
        return gtin
    }

    override fun price(): Double {
        return price
    }

    override fun thumbnailMediaId(): Long {
        return thumbnailMediaId
    }

    override fun productImageIds(): List<Long> {
        return productImageIds
    }
}




