package com.ecommerce.app.product.viewmodel.productoption

import com.ecommerce.app.product.model.ProductOption
import com.ecommerce.app.product.model.ProductOptionValue

data class ProductOptionValueGetVm(
    val id: Long?,
    val productId: Long?,
    val productOption: ProductOption?,
    val displayType: String?,
    val displayOrder: Int?,
    val value: String?
) {
    companion object {
        fun fromModel(productOptionValue: ProductOptionValue): ProductOptionValueGetVm {
            return ProductOptionValueGetVm(
                productOptionValue.id,
                productOptionValue.product?.id,
                productOptionValue.productOption,
                productOptionValue.displayType,
                productOptionValue.displayOrder,
                productOptionValue.value
            )
        }
    }
}
