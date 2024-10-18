package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.ProductVariationSaveVm
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ProductVariationPostVm(
    val name: String,
    val slug: String,
    val sku: String,
    val gtin: String,
    val price: Double,
    val thumbnailMediaId: Long,
    val productImagesIds: List<Long>,
    val optionValuesByOptionId: Map<Long, String>
)
    : ProductVariationSaveVm {
    override fun price(): Double {
        return price
    }

    override fun thumbnailMediaId(): Long {
        return thumbnailMediaId
    }

    override fun productImageIds(): List<Long> {
        return productImagesIds
    }

    override fun optionValuesByOptionId(): Map<Long, String> {
        return optionValuesByOptionId
    }

    override fun id(): Long? {
        return null
    }

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
}
