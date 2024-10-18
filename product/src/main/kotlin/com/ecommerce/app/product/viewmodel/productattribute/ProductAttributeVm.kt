package com.ecommerce.app.product.viewmodel.productattribute

import com.ecommerce.app.product.model.attribute.ProductAttribute

data class ProductAttributeVm(val id: Long?, val name: String?) {
    companion object {
        fun fromModel(productAttribute: ProductAttribute?): ProductAttributeVm {
            return ProductAttributeVm(productAttribute?.id, productAttribute?.name)
        }
    }
}
