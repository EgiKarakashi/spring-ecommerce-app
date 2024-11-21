package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BrandRepository: JpaRepository<Brand, Long> {
    fun findBySlug(slug: String): Optional<Brand>

    @Query("select e from Brand e where e.name = ?1 and (?2 is null or e.id != ?2)")
    fun findExistedName(name: String, id: Long?): Brand

    @Query("SELECT e FROM Brand e WHERE e.name = ?1")
    fun findByName(name: String): Brand?

    @Query("SELECT e FROM Brand e WHERE e.name = ?1 AND e.id != ?2")
    fun findByNameAndNotId(name: String, id: Long): Brand?


    fun findByNameContainingIgnoreCase(name: String): List<Brand>
}
