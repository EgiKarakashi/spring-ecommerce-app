package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.attribute.ProductAttributeGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductAttributeGroupRepository: JpaRepository<ProductAttributeGroup, Long> {

    @Query("SELECT e FROM ProductAttributeGroup  e WHERE e.name = ?1 and (?2 IS NULL OR e.id != ?2)")
    fun findExistedName(name: String, id: Long)
}
