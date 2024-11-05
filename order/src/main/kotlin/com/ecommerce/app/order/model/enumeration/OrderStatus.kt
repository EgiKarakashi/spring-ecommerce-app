package com.ecommerce.app.order.model.enumeration

enum class OrderStatus(val fileName: String) {

    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    PENDING_PAYMENT("PENDING_PAYMENT"),
    PAID("PAID"),
    SHIPPING("SHIPPING"),
    COMPLETED("COMPLETED"),
    REFUND("REFUND"),
    CANCELLED("CANCELLED"),
    REJECT("REJECT");

    fun getName(): String = fileName
}
