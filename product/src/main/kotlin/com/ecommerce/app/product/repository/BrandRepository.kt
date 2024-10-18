package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BrandRepository: JpaRepository<Brand, Long> {
    fun findBySlug(slug: String): Optional<Brand>

    @Query("SELECT e FROM Brand e WHERE e.name = ?1 AND (?2 IS NULL OR e.id != ?2)")
    fun findExistedName(name: String, id: Long): Brand

    fun findByNameContainingIgnoreCase(name: String): List<Brand>
}
