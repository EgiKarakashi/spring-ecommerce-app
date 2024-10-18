package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.Product

data class ProductGetDetailVm(
    val id: Long?,
    val name: String?,
    val slug: String?
) {
    companion object {
        fun fromModel(product: Product): ProductGetDetailVm {
            return ProductGetDetailVm(
                product.id,
                product.name,
                product.slug
            )
        }
    }
}
