package com.ecommerce.app.product.viewmodel.productattribute

import com.ecommerce.app.product.model.attribute.ProductAttribute

data class ProductAttributeGetVm(
    val id: Long,
    val name: String,
    val productAttributeGroup: String
) {
    companion object {
        fun fromModel(productAttribute: ProductAttribute): ProductAttributeGetVm {
            return ProductAttributeGetVm(
                id = productAttribute.id,
                name = productAttribute.name,
                productAttributeGroup = productAttribute.productAttributeGroup.name
            )
        }
    }
}

