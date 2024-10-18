package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Product
import com.ecommerce.app.product.model.ProductOptionValue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductOptionValueRepository: JpaRepository<ProductOptionValue, Long> {

    fun findAllByProduct(product: Product): List<ProductOptionValue>

    fun deleteByProductIdAndValue(productId: Long, value: String)

    fun deleteAllByProductId(productId: Long?)
}
