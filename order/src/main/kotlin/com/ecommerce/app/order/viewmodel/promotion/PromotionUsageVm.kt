package com.ecommerce.app.order.viewmodel.promotion

data class PromotionUsageVm(
    val promotionCode: String,
    val productId: Long,
    val userId: String?,
    val orderId: Long
)
