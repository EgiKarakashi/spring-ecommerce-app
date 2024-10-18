package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.Category
import com.ecommerce.app.product.viewmodel.ImageVm
import com.ecommerce.app.product.viewmodel.productattribute.ProductAttributeValueGetVm

data class ProductDetailInfoVm(
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
    val price: Double?,
    val brandId: Long?,
    val categories: MutableList<Category?>,
    val metaTitle: String?,
    val metaKeyword: String?,
    val metaDescription: String?,
    val taxClassId: Long?,
    val brandName: String?,
    val attributeValues: List<ProductAttributeValueGetVm>?,
    val variations: List<ProductVariationGetVm>?,
    val thumbnail: ImageVm?,
    val productImages: List<ImageVm>?
)
