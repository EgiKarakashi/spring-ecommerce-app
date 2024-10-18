package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.Category
import com.ecommerce.app.product.model.enumeration.DimensionUnit
import com.ecommerce.app.product.viewmodel.ImageVm

data class ProductDetailVm(
    val id: Long?,
    val name: String?,
    val shortDescription: String?,
    val description: String?,
    val specification: String?,
    val sku: String?,
    val gtin: String?,
    val slug: String?,
    val isAllowedToOrder: Boolean?,
    val isPublished: Boolean?,
    val isFeatured: Boolean?,
    val isVisible: Boolean?,
    val stockTrackingEnabled: Boolean?,
    val weight: Double?,
    val dimensionUnit: DimensionUnit?,
    val length: Double?,
    val width: Double?,
    val height: Double?,
    val price: Double?,
    val brandId: Long?,
    val categories: MutableList<Category?>,
    val metaTitle: String?,
    val metaKeyword: String?,
    val metaDescription: String?,
    val thumbnailMedia: ImageVm?,
    val productImageMedias: MutableList<ImageVm?>,
    val taxClassId: Long?
)
