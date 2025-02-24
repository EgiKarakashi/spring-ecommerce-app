package com.ecommerce.app.springecommerceapp.vector.product.service

import com.ecommerce.app.commonlibrary.kafka.cdc.message.Product
import com.ecommerce.app.springecommerceapp.vector.product.store.ProductVectorRepository
import org.springframework.stereotype.Service

@Service
class ProductVectorSyncService(
    val productVectorRepository: ProductVectorRepository
) {

    fun createProductVector(product: Product) {
        if (product.isPublished) {
            productVectorRepository.add(product.id)
        }
    }

    fun updateProductVector(product: Product) {
        if (product.isPublished) {
            productVectorRepository.update(product.id)
        } else {
            productVectorRepository.delete(product.id)
        }
    }

    fun deleteProductVector(productId: Long) {
        productVectorRepository.delete(productId)
    }
}
