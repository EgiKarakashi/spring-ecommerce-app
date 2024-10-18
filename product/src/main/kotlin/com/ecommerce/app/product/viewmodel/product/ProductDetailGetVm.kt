package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeGroupGetVm

data class ProductDetailGetVm(
    val id: Long?,
    val name: String?,
    val brandName: String?,
    val productCategories: List<String?>?,
    val productAttributeGroups: List<ProductAttributeGroupGetVm>?,
    val shortDescription: String?,
    val description: String?,
    val specification: String?,
    val isAllowedToOrder: Boolean?,
    val isPublished: Boolean?,
    val isFeatured: Boolean?,
    val hasOptions: Boolean?,
    val price: Double?,
    val thumbnailMediaUrl: String?,
    val productImageMediaUrls: List<String>?
)
