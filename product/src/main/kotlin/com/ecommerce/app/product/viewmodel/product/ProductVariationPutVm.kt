package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.ProductVariationSaveVm
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import lombok.extern.jackson.Jacksonized

@JsonDeserialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ProductVariationPutVm(
    val id: Long,
    val name: String,
    val slug: String,
    val sku: String,
    val gtin: String,
    val price: Double,
    val thumbnailMediaId: Long,
    val productImageIds: List<Long>,
    val optionValuesByOptionId: Map<Long, String>
): ProductVariationSaveVm {
    override fun price(): Double = price

    override fun thumbnailMediaId(): Long = thumbnailMediaId

    override fun productImageIds(): List<Long> = productImageIds

    override fun optionValuesByOptionId(): Map<Long, String> = optionValuesByOptionId

    override fun id(): Long? = id

    override fun name(): String? = name

    override fun slug(): String? = slug

    override fun sku(): String? = sku

    override fun gtin(): String? = gtin
}
