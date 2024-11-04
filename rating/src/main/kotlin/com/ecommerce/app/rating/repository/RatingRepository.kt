package com.ecommerce.app.rating.repository

import com.ecommerce.app.rating.model.Rating
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.ZonedDateTime

interface RatingRepository: JpaRepository<Rating, Long> {

    fun findByProductId(id: Long, pageable: Pageable): Page<Rating>

    @Query(
        ("SELECT r FROM Rating r "
                + "Where (LOWER(r.productName) LIKE %:productName%) "
                + "AND CONCAT(LOWER(r.firstName), ' ', LOWER(r.lastName)) LIKE %:customerName% "
                + "AND LOWER(r.content) LIKE %:message% "
                + "AND r.createdOn BETWEEN :createdFrom AND :createdTo"))
    fun getRatingListWithFilter(
        @Param("productName") productName: String,
        @Param("customerName") customerName: String,
        @Param("message") message: String,
        @Param("createdFrom") createdFrom: ZonedDateTime,
        @Param("createdTo") createdTo: ZonedDateTime,
        pageable: Pageable
    ): Page<Rating>

    @Query("SELECT r FROM Rating r ORDER BY  r.createdOn DESC ")
    fun getLatestRatings(pageable: Pageable): List<Rating>

    fun existsByCreatedByAndProductId(createdBy: String, productId: Long): Boolean

    @Query("SELECT SUM(r.ratingStar), COUNT(r) FROM Rating r WHERE r.productId = :productId")
    fun getTotalStarsAndTotalRatings(@Param("productId") productId: Long): List<Array<Any>>
}
