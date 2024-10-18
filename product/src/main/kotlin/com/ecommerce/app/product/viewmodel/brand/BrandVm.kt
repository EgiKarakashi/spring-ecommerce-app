package com.ecommerce.app.product.viewmodel.brand

import com.ecommerce.app.product.model.Brand

data class BrandVm(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
    val isPublish: Boolean? = null
) {
    companion object {
        fun fromModel(brand: Brand): BrandVm {
            return BrandVm(brand.id, brand.name, brand.slug, brand.isPublished)
        }
    }
}
