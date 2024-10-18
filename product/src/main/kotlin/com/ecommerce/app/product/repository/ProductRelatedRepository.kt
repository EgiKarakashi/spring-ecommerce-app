package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Product
import com.ecommerce.app.product.model.ProductRelated
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRelatedRepository: JpaRepository<ProductRelated, Long> {

    fun findAllByProduct(product: Product, pageable: Pageable): Page<ProductRelated>
}
