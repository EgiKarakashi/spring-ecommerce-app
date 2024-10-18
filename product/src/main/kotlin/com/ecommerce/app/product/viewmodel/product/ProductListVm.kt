package com.ecommerce.app.product.viewmodel.product

import com.ecommerce.app.product.model.Product
import java.time.ZonedDateTime

data class ProductListVm(
    val id: Long?,
    val name: String?,
    val slug: String?,
    val isAllowedToOrder: Boolean?,
    val isPublished: Boolean?,
    val isFeatured: Boolean?,
    val isVisibleIndividually: Boolean?,
    val price: Double?,
    val createdOn: ZonedDateTime?,
    val taxClassId: Long?,
    val parentId: Long?
) {
    companion object {
        fun fromModel(product: Product): ProductListVm {
            return ProductListVm(
                product.id!!,
                product.name,
                product.slug,
                product.isAllowedToOrder,
                product.isPublished,
                product.isFeatured,
                product.isVisibleIndividually,
                product.price,
                product.createdOn,
                product.taxClassId,
                product.parent?.id
            )
        }
    }
}
