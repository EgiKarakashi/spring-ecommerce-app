package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.ProductOptionValue

data class ProductOptionValueGetVm(
    val id: Long?,
    val productOptionId: Long?,
    val productOptionValue: String?,
    val productOptionName: String?
) {
    companion object {
        fun fromModel(productOptionValue: ProductOptionValue): ProductOptionValueGetVm {
            return ProductOptionValueGetVm(
                productOptionValue.id,
                productOptionValue.productOption?.id,
                productOptionValue.value,
                productOptionValue.productOption?.name
            )
        }
    }
}
