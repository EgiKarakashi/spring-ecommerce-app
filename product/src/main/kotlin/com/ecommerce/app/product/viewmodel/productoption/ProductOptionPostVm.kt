package com.ecommerce.app.product.viewmodel.productoption

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
data class ProductOptionPostVm(
    @field:NotBlank
    val name: String
)
