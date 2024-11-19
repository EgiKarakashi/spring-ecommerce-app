package com.ecommerce.app.tax.repository

import com.ecommerce.app.tax.model.TaxClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TaxClassRepository: JpaRepository<TaxClass, Long> {

    fun existsByName(name: String): Boolean

    @Query("""
         SELECT CASE
                   WHEN count(1)> 0 THEN TRUE
                   ELSE FALSE
                END
         FROM TaxClass tc
         WHERE tc.name = ?1
         AND tc.id != ?2
        """)
    fun existsByNameNotUpdatingTaxClass(name: String, id: Long): Boolean
}
