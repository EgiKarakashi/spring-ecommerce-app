package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.enumeration.DimensionUnit
import com.ecommerce.app.product.validation.ValidateProductPrice
import com.ecommerce.app.product.viewmodel.productoption.ProductOptionValuePutVm
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank
import lombok.extern.jackson.Jacksonized

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize
data class ProductPutVm(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val slug: String,
    val brandId: Long = 0L,
    val categoryIds: List<Long>? = emptyList(),
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
    val productImageIds: List<Long>? = emptyList(),
    val variations: List<ProductVariationPutVm> = emptyList(),
    val productOptionValues: List<ProductOptionValuePutVm> = emptyList(),
    val relatedProductIds: List<Long> = emptyList(),
    val taxClassId: Long = 0L
) : ProductSaveVm<ProductVariationPutVm> {
    override fun variations(): List<ProductVariationPutVm> = variations

    override fun isProductPublished(): Boolean = isPublished
    override fun name(): String?  = name

    override fun slug(): String? = slug

    override fun sku(): String? = sku

    override fun gtin(): String? = gtin

    override fun price(): Double? = price

    override fun thumbnailMediaId(): Long? = thumbnailMediaId

    override fun productImageIds(): List<Long>? = productImageIds
}
