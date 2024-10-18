package com.ecommerce.app.product.viewmodel.product

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
data class ProductQuantityPutVm(
    val productId: Long,
    val quantity: Long
)
