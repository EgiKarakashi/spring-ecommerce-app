package com.ecommerce.app.product.viewmodel.productoption

import com.ecommerce.app.product.model.ProductOption

data class ProductOptionGetVm(
    val id: Long?,
    val name: String?
) {
    companion object {
        fun fromModel(productOption: ProductOption): ProductOptionGetVm {
            return ProductOptionGetVm(
                productOption.id,
                productOption.name
            )
        }
    }
}
