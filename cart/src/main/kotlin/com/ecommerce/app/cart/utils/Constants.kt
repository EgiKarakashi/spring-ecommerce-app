package com.ecommerce.app.cart.utils

class Constants {
    object ErrorCode {
        const val NOT_FOUND_PRODUCT: String = "NOT_FOUND_PRODUCT"
        const val NOT_EXISTING_ITEM_IN_CART: String = "NOT_EXISTING_ITEM_IN_CART"
        const val NOT_EXISTING_PRODUCT_IN_CART: String = "NOT_EXISTING_PRODUCT_IN_CART"
        const val NON_EXISTING_CART_ITEM: String = "NON_EXISTING_CART_ITEM"
        const val ADD_CART_ITEM_FAILED: String = "ADD_CART_ITEM_FAILED"
        const val DUPLICATED_CART_ITEMS_TO_DELETE: String = "DUPLICATED_CART_ITEMS_TO_DELETE"
    }
}
