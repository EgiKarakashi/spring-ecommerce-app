package com.ecommerce.app.product.viewmodel.category

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.NotBlank

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
data class CategoryPostVm(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val slug: String,
    val description: String?,
    val parentId: Long?,
    val metaKeywords: String?,
    val metaDescription: String?,
    val displayOrder: Short?,
    val isPublish: Boolean?,
    val imageId: Long?
)
