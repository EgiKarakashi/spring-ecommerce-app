package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.ProductOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductOptionRepository: JpaRepository<ProductOption, Long> {

    fun findAllByIdIn(ids: List<Long>): List<ProductOption>

    @Query("SELECT e FROM ProductOption  e WHERE e.name = ?1 and (?2 IS NULL OR e.id != ?2)")
    fun findExistedName(name: String, id: Long): ProductOption
}
