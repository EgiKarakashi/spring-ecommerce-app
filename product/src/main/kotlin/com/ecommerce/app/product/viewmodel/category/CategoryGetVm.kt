package com.ecommerce.app.product.viewmodel.category

import com.ecommerce.app.product.model.Category
import com.ecommerce.app.product.viewmodel.ImageVm

data class CategoryGetVm(
    val id: Long?,
    val name: String?,
    val slug: String?,
    val parentId: Long,
    val categoryImage: ImageVm?
) {

    companion object {
        fun fromModel(category: Category): CategoryGetVm {
            val parent = category.parent
            val parentId = parent?.id ?: 1L
            return CategoryGetVm(
                category.id,
                category.name,
                category.slug,
                parentId,
                null
            )
        }
    }
}
