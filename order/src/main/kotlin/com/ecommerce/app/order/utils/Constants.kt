package com.ecommerce.app.order.utils

object Constants {

    object ErrorCode {
        const val ORDER_NOT_FOUND = "ORDER_NOT_FOUND"
        const val CHECKOUT_NOT_FOUND = "CHECKOUT_NOT_FOUND"
        const val SIGN_IN_REQUIRED = "SIGN_IN_REQUIRED"
        const val FORBIDDEN = "FORBIDDEN"
    }

    object Column {
        // Common columns
        const val ID_COLUMN = "id"
        const val CREATE_ON_COLUMN = "createdOn"
        const val CREATE_BY_COLUMN = "createdBy"

        // Order entity
        const val ORDER_EMAIL_COLUMN = "email"
        const val ORDER_PHONE_COLUMN = "phone"
        const val ORDER_ORDER_ID_COLUMN = "orderId"
        const val ORDER_ORDER_STATUS_COLUMN = "orderStatus"
        const val ORDER_COUNTRY_NAME_COLUMN = "countryName"
        const val ORDER_SHIPPING_ADDRESS_ID_COLUMN = "shippingAddressId"
        const val ORDER_BILLING_ADDRESS_ID_COLUMN = "billingAddressId"

        // OrderItem entity
        const val ORDER_ITEM_PRODUCT_ID_COLUMN = "productId"
        const val ORDER_ITEM_PRODUCT_NAME_COLUMN = "productName"
    }
}

