package com.ecommerce.app.search.model

import com.ecommerce.app.search.constant.enums.SortType

data class ProductCriteriaDto(
    val keyword: String,
    val page: Int,
    val size: Int,
    val brand: String?,
    val category: String?,
    val attribute: String?,
    val minPrice: Double?,
    val maxPrice: Double?,
    val sortType: SortType
)
