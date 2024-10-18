package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.ProductVariationSaveVm

interface ProductSaveVm<T: ProductVariationSaveVm>: ProductProperties {
    fun variations(): List<T>
    fun isProductPublished(): Boolean

    override fun id(): Long? {
        return null
    }
}
