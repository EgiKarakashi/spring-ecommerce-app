package com.ecommerce.app.search.viewmodel

import com.ecommerce.app.search.model.Product
import java.time.ZonedDateTime

data class ProductGetVm(
    val id: Long,
    val name: String,
    val slug: String,
    val thumbnailId: Long,
    val price: Double,
    val isAllowedToOrder: Boolean,
    val isPublished: Boolean,
    val isFeatured: Boolean,
    val isVisibleIndividually: Boolean,
    val createdOn: ZonedDateTime
) {
    companion object {
        fun fromModel(product: Product): ProductGetVm {
            return ProductGetVm(
                product.id,
                product.name,
                product.slug,
                product.thumbnailMediaId,
                product.price,
                product.isAllowedToOrder,
                product.isPublished,
                product.isFeatured,
                product.isVisibleIndividually,
                product.createdOn!!
            )
        }
    }
}
