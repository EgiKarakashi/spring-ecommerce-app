package com.ecommerce.app.rating.viewmodel

import com.ecommerce.app.rating.model.Rating
import java.time.ZonedDateTime

data class RatingVm(
    val id: Long?,
    val content: String?,
    val star: String?,
    val productId: Long?,
    val productName: String?,
    val createdBy: String?,
    val lastName: String?,
    val firstName: String?,
    val createdOn: ZonedDateTime?
) {
    companion object {
        fun fromModel(rating: Rating): RatingVm {
            return RatingVm(
                rating.id,
                rating.content,
                rating.ratingStar,
                rating.productId,
                rating.productName,
                rating.createdBy,
                rating.lastName,
                rating.firstName,
                rating.createdOn
            )
        }
    }
}

