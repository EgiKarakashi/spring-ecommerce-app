package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.attribute.ProductAttribute
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductAttributeRepository: JpaRepository<ProductAttribute, Long> {

    @Query("SELECT e FROM ProductAttribute  e WHERE e.name = ?1 AND (?2 IS NULL OR e.id != ?2)")
    fun findExistedName(name: String, id: Long)
}
