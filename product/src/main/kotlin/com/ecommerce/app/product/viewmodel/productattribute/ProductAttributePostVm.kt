package com.ecommerce.app.product.viewmodel.productattribute

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
data class ProductAttributePostVm(
    @field:NotBlank
    val name: String,
    val productAttributeGroupId: Long
)
