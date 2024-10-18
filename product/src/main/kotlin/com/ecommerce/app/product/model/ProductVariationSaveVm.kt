package com.ecommerce.app.product.model

import com.ecommerce.app.product.viewmodel.product.ProductProperties

interface ProductVariationSaveVm: ProductProperties {

    override fun price(): Double

    override fun thumbnailMediaId(): Long

    override fun productImageIds(): List<Long>

    fun optionValuesByOptionId(): Map<Long, String>
}
