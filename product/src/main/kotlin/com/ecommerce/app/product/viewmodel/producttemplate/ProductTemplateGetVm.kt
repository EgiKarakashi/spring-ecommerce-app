package com.ecommerce.app.product.viewmodel.producttemplate

import com.ecommerce.app.product.model.attribute.ProductTemplate

data class ProductTemplateGetVm(val id: Long?, val name: String?) {
    companion object {
        fun fromModel(productTemplate: ProductTemplate): ProductTemplateGetVm {
            return ProductTemplateGetVm(
                productTemplate.id,
                productTemplate.name
            )
        }
    }
}
