package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Product
import com.ecommerce.app.product.model.ProductOptionCombination
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProductOptionCombinationRepository: JpaRepository<ProductOptionCombination, Long> {

    fun findAllByProduct(product: Product): List<ProductOptionCombination>

    fun findByProductId(productId: Long): Optional<ProductOptionCombination>

    fun deleteByProductId(productId: Long)
}
