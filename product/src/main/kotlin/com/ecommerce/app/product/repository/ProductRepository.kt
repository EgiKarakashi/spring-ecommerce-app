package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.Brand
import com.ecommerce.app.product.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
    fun findAllByBrandAndIsPublishedTrueOrderByIdAsc(brand: Brand): List<Product>

    fun findAllByBrandAndIsPublishedTrue(brand: Brand): List<Product>

    fun findBySlugAndIsPublishedTrue(slug: String): Optional<Product>

    fun findByGtinAndIsPublishedTrue(gtin: String): Optional<Product>

    fun findBySkuAndIsPublishedTrue(sku: String): Optional<Product>

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:productName% " +
            "AND (p.brand.name IN :brandName OR (:brandName IS NULL OR :brandName = '')) " +
            "AND p.isVisibleIndividually = TRUE " +
            "AND p.isPublished = TRUE " +
            "ORDER BY p.lastModifiedOn DESC")
    fun getProductsWithFilter(@Param("productName") productName: String,
                                                @Param("brandName") brandName: String,
                                                pageable: Pageable): Page<Product>

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:productName% " +
            "AND (p.brand.name IN :brandName OR (:brandName IS NULL OR :brandName = '')) " +
            "AND p.isVisibleIndividually = TRUE " +
            "AND p.isPublished = TRUE " +
            "ORDER BY p.lastModifiedOn DESC")
    fun  getExportingProducts(@Param("productName") productName: String,
                                            @Param("brandName") brandName: String): List<Product>


    fun findAllByIdIn(productIds: List<Long>): List<Product>

    @Query("FROM Product p WHERE p.isFeatured = TRUE " +
                    "AND p.isVisibleIndividually = TRUE " +
                    "AND p.isPublished = TRUE ORDER BY p.lastModifiedOn DESC")
    fun getFeaturedProduct(pageable: Pageable): Page<Product>

    @Query("SELECT p FROM Product p LEFT JOIN p.productCategories pc LEFT JOIN pc.category c " +
            "WHERE LOWER(p.name) LIKE %:productName% " +
            "AND (c.slug = :categorySlug OR (:categorySlug IS NULL OR :categorySlug = '')) " +
            "AND (:startPrice IS NULL OR p.price >= :startPrice) " +
            "AND (:endPrice IS NULL OR p.price <= :endPrice) " +
            "AND p.isVisibleIndividually = TRUE " +
            "AND p.isPublished = TRUE " +
            "ORDER BY p.id DESC")
    fun findByProductNameAndCategorySlugAndPriceBetween(
        @Param("productName") productName: String,
        @Param("categorySlug") categorySlug: String,
        @Param("startPrice") startPrice: Double?,
        @Param("endPrice") endPrice: Double?,
        pageable: Pageable
    ): Page<Product>

    @Query("SELECT p FROM Product p "
    + "WHERE (LOWER(p.name) LIKE concat('%', LOWER(:name), '%') "
    + "OR LOWER(p.sku) LIKE concat('%', LOWER(:sku), '%')) "
    + "AND ((:selection = 'ALL') "
    + "OR ((:selection = 'YES' and p.id in :productIds ) "
    + "OR (:selection = 'NO' and ((coalesce(:productIds) is null) or p.id not in :productIds)))) "
    + "ORDER BY p.id ASC ")
    fun findProductForWarehouse(@Param("name") name: String, @Param("sku") sku: String, @Param("productIds") productIds: List<Long>?,
           @Param("selection") selection: String): List<Product>

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productCategories pc WHERE pc.id IN :categoryIds")
    fun findByCategoryIdsIn(@Param("categoryIds") categoryIds: List<Long>): List<Product>

    @Query("SELECT p FROM Product p JOIN p.brand b WHERE b.id IN :brandIds")
    fun findByBrandIdsIn(@Param("brandIds") brandIds: List<Long>): List<Product>

    @Query("SELECT  p FROM Product p ORDER BY p.createdOn DESC")
    fun getLatestProducts(pageable: Pageable): List<Product>
}
