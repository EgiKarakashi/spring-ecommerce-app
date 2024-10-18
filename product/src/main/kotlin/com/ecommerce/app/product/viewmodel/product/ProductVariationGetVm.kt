package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.viewmodel.ImageVm

data class ProductVariationGetVm(
    val id: Long?,
    val name: String?,
    val slug: String?,
    val sku: String?,
    val gtin: String?,
    val price: Double?,
    val thumbnail: ImageVm?,
    val productImages: List<ImageVm>?,
    val options: Map<Long?, String?>
)
