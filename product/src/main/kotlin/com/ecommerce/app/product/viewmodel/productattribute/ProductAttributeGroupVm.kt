package com.ecommerce.app.product.viewmodel.productattribute

import com.ecommerce.app.product.model.attribute.ProductAttributeGroup

data class ProductAttributeGroupVm(
    val id: Long?,
   val name: String?
) {
    companion object {
        fun fromModel(productAttributeGroup: ProductAttributeGroup): ProductAttributeGroupVm {
            return ProductAttributeGroupVm(
                productAttributeGroup.id,
                productAttributeGroup.name
            )
        }
    }
}
