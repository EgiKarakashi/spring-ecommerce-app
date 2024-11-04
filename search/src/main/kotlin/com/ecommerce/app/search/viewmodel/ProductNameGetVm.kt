package com.ecommerce.app.search.viewmodel

import com.ecommerce.app.search.model.Product

data class ProductNameGetVm(
    val name: String
) {
    companion object {
        fun fromModel(product: Product): ProductNameGetVm {
            return ProductNameGetVm(product.name)
        }
    }
}
