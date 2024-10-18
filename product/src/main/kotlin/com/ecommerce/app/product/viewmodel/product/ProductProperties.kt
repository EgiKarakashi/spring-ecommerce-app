package com.ecommerce.app.product.viewmodel.product

interface ProductProperties {

    fun id(): Long?

    fun name(): String?

    fun slug(): String?

    fun sku(): String?

    fun gtin(): String?

    fun price(): Double?

    fun thumbnailMediaId(): Long?

    fun productImageIds(): List<Long>?
}
