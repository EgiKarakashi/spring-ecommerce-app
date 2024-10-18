package com.ecommerce.app.product.repository

import com.ecommerce.app.product.model.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepository: JpaRepository<ProductImage, Long> {

    @Modifying
    @Query("DELETE FROM ProductImage  p WHERE p.product.id = :productId AND p.imageId IN :imageIds")
    fun deleteByImageIdInAndProductId(imageIds: List<Long?>, productId: Long?)

    @Modifying
    @Query("DELETE FROM ProductImage p WHERE p.product.id = :productId")
    fun deleteByProductId(productId: Long)
}
