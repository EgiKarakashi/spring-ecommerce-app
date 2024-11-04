package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CategoryRepository: JpaRepository<Category, Long> {
    fun findBySlug(slug: String): Optional<Category>

    @Query("select e from Category e where e.name = ?1 and (?2 is null or e.id != ?2)")
//@Query("select e from Category e where e.name = ?1 and e.id = ?2")
    fun findExistedName(name: String, id: Long): Category

    fun findByNameContainingIgnoreCase(name: String): List<Category>
}
