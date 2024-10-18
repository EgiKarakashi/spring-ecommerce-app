package com.ecommerce.app.product.viewmodel.product

data class ProductFeatureGetVm(
    val productList: List<ProductThumbnailGetVm>,
    val totalPage: Int
)
