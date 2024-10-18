package com.ecommerce.app.product.viewmodel.producttemplate

import com.ecommerce.app.product.model.attribute.ProductAttributeTemplate
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
data class ProductTemplatePostVm(
    @field:NotBlank
    val name: String,
    val productAttributeTemplates: List<ProductAttributeTemplate>
)
