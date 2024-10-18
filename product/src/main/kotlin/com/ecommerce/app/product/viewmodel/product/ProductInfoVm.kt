package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.Product

data class ProductInfoVm(
    val id: Long?,
    val name: String?,
    val sku: String?
) {
    companion object {
        fun fromProduct(product: Product): ProductInfoVm {
            return ProductInfoVm(
                product.id,
                product.name,
                product.sku
            )
        }
    }
}
