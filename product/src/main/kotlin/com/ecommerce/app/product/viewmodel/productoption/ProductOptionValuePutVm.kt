package com.ecommerce.app.product.viewmodel.productoption

import com.ecommerce.app.product.model.ProductOptionValueSaveVm

abstract class ProductOptionValuePutVm(
    val productOptionId: Long,
    val displayType: String,
    val displayOrder: Int,
    val value: List<String>
): ProductOptionValueSaveVm
