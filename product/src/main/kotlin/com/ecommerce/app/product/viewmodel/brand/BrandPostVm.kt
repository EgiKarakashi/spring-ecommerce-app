package com.ecommerce.app.product.viewmodel.brand

import com.ecommerce.app.product.model.Brand
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
data class BrandPostVm(
    @field:NotBlank
    var name: String,

    @field:NotBlank
    var slug: String,

    var isPublish: Boolean
) {
    fun toModel(): Brand {
        val brand = Brand()
        brand.name = name
        brand.slug = slug
        brand.isPublished = isPublish
        return brand
    }
}
