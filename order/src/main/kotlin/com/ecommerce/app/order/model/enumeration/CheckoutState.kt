package com.ecommerce.app.order.model.enumeration

enum class CheckoutState(val displayName: String) {
    COMPLETED("Completed"),
    PENDING("Pending"),
    LOCK("LOCK");

    fun getName(): String = displayName
}

