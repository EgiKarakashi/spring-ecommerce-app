package com.ecommerce.app.product.viewmodel.category

import com.ecommerce.app.product.model.Category
import com.ecommerce.app.product.viewmodel.ImageVm

data class CategoryGetDetailVm(
    val id: Long?,
    val name: String?,
    val slug: String?,
    val description: String?,
    val parentId: Long?,
    val metaKeywords: String?,
    val metaDescription: String?,
    val displayOrder: Short?,
    val isPublish: Boolean?,
    val categoryImage: ImageVm?
) {
    companion object {
        fun fromModel(category: Category): CategoryGetDetailVm {
            val parentId = category.parent?.id ?: 1L
            return CategoryGetDetailVm(
                id = category.id,
                name = category.name,
                slug = category.slug,
                description = category.description,
                parentId = parentId,
                metaKeywords = category.metaKeyword,
                metaDescription = category.metaDescription,
                displayOrder = category.displayOrder,
                isPublish = category.isPublished,
                categoryImage = null
            )
        }
    }
}
