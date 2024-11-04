package com.ecommerce.app.rating.viewmodel

data class RatingListVm(
    val ratingList: List<RatingVm>,
    val totalElements: Long,
    val totalPages: Int
)
