package com.ecommerce.app.product.viewmodel.productattribute

import com.ecommerce.app.product.model.attribute.ProductAttributeGroup
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
data class ProductAttributeGroupPostVm(
    @field:NotBlank
    val name: String
) {
    fun toModel(): ProductAttributeGroup {
        val productAttributeGroup = ProductAttributeGroup()
        productAttributeGroup.name = name
        return productAttributeGroup
    }
}
