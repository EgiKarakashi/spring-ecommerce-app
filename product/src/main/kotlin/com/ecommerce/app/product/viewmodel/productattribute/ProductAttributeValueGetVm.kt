package com.ecommerce.app.product.viewmodel.productattribute

import com.ecommerce.app.product.model.attribute.ProductAttributeValue

data class ProductAttributeValueGetVm(
    val id: Long?,
    val nameProductAttribute: String?,
    val value: String?
) {
    companion object {
        fun fromModel(productAttributeValue: ProductAttributeValue): ProductAttributeValueGetVm {
            return ProductAttributeValueGetVm(
                productAttributeValue.id,
                productAttributeValue.productAttribute?.name,
                productAttributeValue.value
            )
        }
    }
}
