package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Category
import com.ecommerce.app.product.model.ProductCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryRepository: JpaRepository<ProductCategory, Long> {

    fun findAllByCategory(pageable: Pageable, category: Category): Page<ProductCategory>

    fun findAllByProductId(productId: Long): List<ProductCategory>
}
