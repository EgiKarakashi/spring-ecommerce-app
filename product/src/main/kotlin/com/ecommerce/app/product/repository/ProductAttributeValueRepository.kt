package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Product
import com.ecommerce.app.product.model.attribute.ProductAttributeValue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductAttributeValueRepository: JpaRepository<ProductAttributeValue, Long> {

    fun findAllByProduct(product: Product): List<ProductAttributeValue>
}
