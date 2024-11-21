package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.attribute.ProductAttribute
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

interface ProductAttributeRepository : JpaRepository<ProductAttribute, Long> {
    @Query("SELECT pa FROM ProductAttribute pa WHERE pa.name = :name AND (:id IS NULL OR pa.id <> :id)")
    fun findExistedName(@Param("name") name: String, @Param("id") id: Long?): ProductAttribute?
}
