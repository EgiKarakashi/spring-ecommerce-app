package com.ecommerce.app.product.viewmodel.producttemplate

import com.ecommerce.app.product.model.attribute.ProductAttributeTemplate
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeVm

data class ProductAttributeTemplateGetVm(val productAttributeVm: ProductAttributeVm?, val displayOrder: Int?) {
    companion object {
        fun fromModel(productAttributeTemplate: ProductAttributeTemplate): ProductAttributeTemplateGetVm {
            return ProductAttributeTemplateGetVm(
                ProductAttributeVm.fromModel(productAttributeTemplate.productAttribute),
                productAttributeTemplate.displayOrder
            )
        }
    }
}
